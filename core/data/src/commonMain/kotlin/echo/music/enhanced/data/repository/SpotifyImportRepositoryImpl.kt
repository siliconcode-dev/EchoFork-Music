@file:OptIn(ExperimentalTime::class)

package echo.music.enhanced.data.repository

import echo.music.enhanced.domain.data.entities.SongEntity
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.domain.repository.LocalPlaylistRepository
import echo.music.enhanced.domain.repository.SearchRepository
import echo.music.enhanced.domain.repository.SpotifyImportProgress
import echo.music.enhanced.domain.repository.SpotifyImportRepository
import echo.music.enhanced.domain.repository.SpotifyImportSource
import echo.music.enhanced.domain.repository.SpotifyImportSourceSummary
import echo.music.enhanced.domain.repository.SpotifyImportState
import echo.music.enhanced.domain.repository.SpotifyImportSummary
import echo.music.enhanced.domain.utils.Resource
import echo.music.enhanced.domain.utils.toSongEntity
import echo.music.enhanced.domain.utils.toTrack
import echo.music.enhanced.spotify.Spotify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.lastOrNull
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Orchestrates "Import from Spotify" (v0.1.15): reuses this fork's existing `sp_dc`+TOTP Spotify
 * session (already used for Canvas/Lyrics) to list every playlist the user owns or follows plus
 * Liked Songs, paginates each source's tracks, resolves them to real YT Music songs via a
 * bigram-title/artist + duration match scorer (ported from upstream Echo Music's real
 * `SpotifyMapper`), and mirrors the result into local playlists — refreshing in place on re-import
 * rather than duplicating, tracked via [DataStoreManager.spotifyImportPlaylistMap].
 */
internal class SpotifyImportRepositoryImpl(
    private val spotify: Spotify,
    private val dataStoreManager: DataStoreManager,
    private val searchRepository: SearchRepository,
    private val localPlaylistRepository: LocalPlaylistRepository,
) : SpotifyImportRepository {
    override fun importAllPlaylists(): Flow<SpotifyImportState> =
        flow {
            val accessToken = getAccessToken()
            if (accessToken == null) {
                emit(SpotifyImportState.Failed("Not connected to Spotify."))
                return@flow
            }

            val sources = loadSources(accessToken)
            val importMap = parseImportMap(dataStoreManager.spotifyImportPlaylistMap.first()).toMutableMap()
            val summaries = mutableListOf<SpotifyImportSourceSummary>()

            sources.forEachIndexed { sourceIndex, source ->
                emit(
                    SpotifyImportState.Progress(
                        SpotifyImportProgress(source.title, sourceIndex, sources.size, 0, source.trackCount ?: 0),
                    ),
                )

                val tracks = fetchAllTracks(accessToken, source)
                val resolved = mutableListOf<SongEntity>()
                tracks.forEach { track ->
                    resolveSong(track)?.let { resolved += it }
                    emit(
                        SpotifyImportState.Progress(
                            SpotifyImportProgress(source.title, sourceIndex, sources.size, resolved.size, tracks.size),
                        ),
                    )
                }

                val existingId = importMap[source.stableId]
                if (existingId != null) {
                    localPlaylistRepository.replaceLocalPlaylistTracks(existingId, resolved).lastOrNull()
                } else {
                    val newId = localPlaylistRepository.createLocalPlaylistWithTracks(source.title, resolved).lastOrNull()?.data
                    if (newId != null) importMap[source.stableId] = newId
                }

                summaries += SpotifyImportSourceSummary(source.title, tracks.size, resolved.size, tracks.size - resolved.size)
            }

            dataStoreManager.setSpotifyImportPlaylistMap(serializeImportMap(importMap))
            emit(SpotifyImportState.Done(SpotifyImportSummary(summaries)))
        }.catch { e ->
            emit(SpotifyImportState.Failed(e.message ?: "Import failed."))
        }.flowOn(Dispatchers.IO)

    /** Reuses the same cached personal-token DataStore fields Canvas/Lyrics already mint via TOTP. */
    private suspend fun getAccessToken(): String? {
        val expires = dataStoreManager.spotifyPersonalTokenExpires.first()
        val cached = dataStoreManager.spotifyPersonalToken.first()
        if (cached.isNotEmpty() && expires != 0L && expires > Clock.System.now().toEpochMilliseconds()) {
            return cached
        }
        val spdc = dataStoreManager.spdc.first()
        if (spdc.isEmpty()) return null
        val token = spotify.getPersonalTokenWithTotp(spdc).getOrNull() ?: return null
        dataStoreManager.setSpotifyPersonalToken(token.accessToken)
        dataStoreManager.setSpotifyPersonalTokenExpires(token.accessTokenExpirationTimestampMs)
        return token.accessToken
    }

    private suspend fun loadSources(accessToken: String): List<SpotifyImportSource> {
        val sources = mutableListOf<SpotifyImportSource>()
        val likedTotal = spotify.getLikedSongs(accessToken, limit = 1, offset = 0).getOrNull()?.total ?: 0
        sources += SpotifyImportSource.LikedSongs(trackCount = likedTotal)

        var offset = 0
        val limit = 50
        while (true) {
            val page = spotify.getLibraryPlaylists(accessToken, limit, offset).getOrNull() ?: break
            if (page.items.isEmpty()) break
            page.items.forEach { playlist ->
                if (playlist.id.isNotBlank()) {
                    sources += SpotifyImportSource.Playlist(playlist.id, playlist.name, playlist.trackCount, playlist.thumbnailUrl)
                }
            }
            offset += page.items.size
            if (offset >= page.total || page.items.size < limit) break
        }
        return sources
    }

    private suspend fun fetchAllTracks(
        accessToken: String,
        source: SpotifyImportSource,
    ): List<Spotify.SpotifyImportTrack> {
        val tracks = mutableListOf<Spotify.SpotifyImportTrack>()
        var offset = 0
        val limit = 100
        while (true) {
            val page =
                when (source) {
                    is SpotifyImportSource.LikedSongs -> spotify.getLikedSongs(accessToken, limit, offset).getOrNull()
                    is SpotifyImportSource.Playlist -> spotify.getPlaylistTracks(accessToken, source.spotifyId, limit, offset).getOrNull()
                } ?: break
            if (page.items.isEmpty()) break
            tracks += page.items.filter { it.title.isNotBlank() }
            offset += page.items.size
            if (offset >= page.total || page.items.size < limit) break
        }
        return tracks
    }

    private suspend fun resolveSong(track: Spotify.SpotifyImportTrack): SongEntity? {
        val query = if (track.artist.isBlank()) track.title else "${track.artist} ${track.title}"
        val resource = searchRepository.getSearchDataSong(query).lastOrNull()
        val candidates = (resource as? Resource.Success)?.data ?: return null
        val best =
            candidates.maxByOrNull { candidate ->
                matchScore(
                    title = track.title,
                    artist = track.artist,
                    durationMs = track.durationMs,
                    candidateTitle = candidate.title.orEmpty(),
                    candidateArtist = candidate.artists?.joinToString(" ") { it.name }.orEmpty(),
                    candidateDurationSec = candidate.durationSeconds,
                )
            }
        return best?.toTrack()?.toSongEntity()
    }

    // ── Match scoring, ported from upstream Echo Music's real SpotifyMapper (title/artist bigram
    // similarity + duration closeness) — run once per track during a single sequential import
    // pass, so upstream's LRU normalization/bigram caches aren't needed here.

    private fun normalizeTitle(title: String): String =
        title
            .lowercase()
            .replace(FEAT_PATTERN, "")
            .replace(FT_PATTERN, "")
            .replace(BRACKET_PATTERN, "")
            .replace(REMASTER_PATTERN, "")
            .replace(REMIX_PATTERN, "")
            .replace(NON_ALNUM_PATTERN, "")
            .replace(MULTI_SPACE_PATTERN, " ")
            .trim()

    private fun bigrams(normalized: String): Set<String> = if (normalized.length < 2) emptySet() else normalized.windowed(2).toSet()

    private fun bigramSimilarity(
        a: String,
        bigramsA: Set<String>,
        b: String,
        bigramsB: Set<String>,
    ): Double {
        if (a == b) return 1.0
        if (bigramsA.isEmpty() || bigramsB.isEmpty()) return 0.0
        val intersection = bigramsA.count { it in bigramsB }
        return (2.0 * intersection) / (bigramsA.size + bigramsB.size)
    }

    private fun durationScore(
        spotifyDurationMs: Int,
        candidateDurationSec: Int?,
    ): Double {
        if (candidateDurationSec == null || spotifyDurationMs <= 0) return 0.5
        val diff = kotlin.math.abs(spotifyDurationMs / 1000 - candidateDurationSec)
        return when {
            diff <= 2 -> 1.0
            diff <= 5 -> 0.8
            diff <= 10 -> 0.5
            diff <= 30 -> 0.2
            else -> 0.0
        }
    }

    private fun matchScore(
        title: String,
        artist: String,
        durationMs: Int,
        candidateTitle: String,
        candidateArtist: String,
        candidateDurationSec: Int?,
    ): Double {
        val normTitle = normalizeTitle(title)
        val normCandidateTitle = normalizeTitle(candidateTitle)
        val normArtist = normalizeTitle(artist)
        val normCandidateArtist = normalizeTitle(candidateArtist)
        val titleScore = bigramSimilarity(normTitle, bigrams(normTitle), normCandidateTitle, bigrams(normCandidateTitle))
        val artistScore = bigramSimilarity(normArtist, bigrams(normArtist), normCandidateArtist, bigrams(normCandidateArtist))
        val durScore = durationScore(durationMs, candidateDurationSec)
        return titleScore * 0.45 + artistScore * 0.35 + durScore * 0.20
    }

    // ── Stable-id -> local-playlist-id map, serialized as "key=id,key=id,..." (Spotify ids and our
    // own stable-id prefixes are always plain alphanumeric/underscore, so no escaping is needed).

    private fun parseImportMap(serialized: String): Map<String, Long> =
        if (serialized.isBlank()) {
            emptyMap()
        } else {
            serialized
                .split(",")
                .mapNotNull { entry ->
                    val parts = entry.split("=")
                    if (parts.size == 2) parts[1].toLongOrNull()?.let { parts[0] to it } else null
                }.toMap()
        }

    private fun serializeImportMap(map: Map<String, Long>): String = map.entries.joinToString(",") { "${it.key}=${it.value}" }

    private companion object {
        val FEAT_PATTERN = Regex("\\(feat\\..*?\\)")
        val FT_PATTERN = Regex("\\(ft\\..*?\\)")
        val BRACKET_PATTERN = Regex("\\[.*?]")
        val REMASTER_PATTERN = Regex("\\(.*?remaster.*?\\)", RegexOption.IGNORE_CASE)
        val REMIX_PATTERN = Regex("\\(.*?remix.*?\\)", RegexOption.IGNORE_CASE)
        val NON_ALNUM_PATTERN = Regex("[^a-z0-9\\s]")
        val MULTI_SPACE_PATTERN = Regex("\\s+")
    }
}
