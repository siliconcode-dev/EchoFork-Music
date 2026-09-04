package echo.music.enhanced.data.repository

import echo.music.enhanced.aiservice.AiClient
import echo.music.enhanced.domain.data.entities.SongEntity
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.domain.repository.AiPlaylistRepository
import echo.music.enhanced.domain.repository.LocalPlaylistRepository
import echo.music.enhanced.domain.repository.SearchRepository
import echo.music.enhanced.domain.repository.SongRepository
import echo.music.enhanced.domain.utils.LocalResource
import echo.music.enhanced.domain.utils.Resource
import echo.music.enhanced.domain.utils.toSongEntity
import echo.music.enhanced.domain.utils.toTrack
import echo.music.enhanced.domain.utils.wrapDataResource
import echo.music.enhanced.domain.utils.wrapMessageResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.lastOrNull

internal class AiPlaylistRepositoryImpl(
    private val aiClient: AiClient,
    private val searchRepository: SearchRepository,
    private val localPlaylistRepository: LocalPlaylistRepository,
    private val songRepository: SongRepository,
    private val dataStoreManager: DataStoreManager,
) : AiPlaylistRepository {
    private suspend fun resolveSong(
        title: String,
        artist: String,
    ): SongEntity? {
        val resource = searchRepository.getSearchDataSong("$title $artist").lastOrNull()
        val results = (resource as? Resource.Success)?.data
        return results?.firstOrNull()?.toTrack()?.toSongEntity()
    }

    private fun SongEntity.toAiLabel(): String {
        val artist = artistName?.joinToString(", ") ?: ""
        return if (artist.isEmpty()) title else "$title by $artist"
    }

    override fun generatePlaylist(
        prompt: String,
        songCount: Int,
    ): Flow<LocalResource<Long>> =
        wrapDataResource {
            val suggestion = aiClient.generatePlaylist(prompt, songCount).getOrThrow()
            val resolved = suggestion.songs.mapNotNull { resolveSong(it.title, it.artist) }
            if (resolved.isEmpty()) {
                throw IllegalStateException("Failed to find any of the suggested songs.")
            }
            val name = suggestion.name.ifBlank { prompt.take(40) }
            localPlaylistRepository.createLocalPlaylistWithTracks(name, resolved).lastOrNull()?.data
                ?: throw IllegalStateException("Failed to create playlist.")
        }

    override fun modifyPlaylist(
        playlistId: Long,
        prompt: String,
    ): Flow<LocalResource<String>> =
        wrapMessageResource(successMessage = "") {
            val existingSongs = localPlaylistRepository.getFullPlaylistTracks(playlistId)
            val songLabels = existingSongs.map { it.videoId to it.toAiLabel() }
            val modification = aiClient.modifyPlaylist(songLabels, prompt).getOrThrow()
            modification.removeIds.forEach { id ->
                existingSongs.find { it.videoId == id }?.let { song ->
                    localPlaylistRepository.removeTrackFromLocalPlaylist(playlistId, song, "", "", "").lastOrNull()
                }
            }
            modification.additions.forEach { addition ->
                resolveSong(addition.title, addition.artist)?.let { song ->
                    localPlaylistRepository.addTrackToLocalPlaylist(playlistId, song, "", "", "").lastOrNull()
                }
            }
        }

    override fun generateRecommendations(songCount: Int): Flow<LocalResource<Long>> =
        wrapDataResource {
            val librarySongs = songRepository.getMostPlayedSongs().first().take(50)
            val librarySongTitles = librarySongs.map { it.toAiLabel() }
            val suggestion = aiClient.generateRecommendations(librarySongTitles, songCount).getOrThrow()
            val resolved = suggestion.songs.mapNotNull { resolveSong(it.title, it.artist) }
            if (resolved.isEmpty()) {
                throw IllegalStateException("Failed to find any recommended songs.")
            }
            val existingId = dataStoreManager.aiRecommendationsPlaylistId.first()
            if (existingId != 0L) {
                localPlaylistRepository.replaceLocalPlaylistTracks(existingId, resolved).lastOrNull()
                existingId
            } else {
                val newId =
                    localPlaylistRepository.createLocalPlaylistWithTracks("Recommended by AI", resolved).lastOrNull()?.data
                        ?: throw IllegalStateException("Failed to create recommendations playlist.")
                dataStoreManager.setAiRecommendationsPlaylistId(newId)
                newId
            }
        }
}
