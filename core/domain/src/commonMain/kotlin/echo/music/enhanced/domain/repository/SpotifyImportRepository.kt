package echo.music.enhanced.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * One thing to pull from a Spotify account: either the user's Liked Songs, or a single playlist
 * they own or follow. [stableId] is a durable key (independent of this app's own local playlist
 * IDs) used to recognize "this is the same Spotify source as last time" across repeated imports —
 * mirrors upstream Echo Music's own real Import-from-Spotify feature's `localPlaylistId` convention.
 */
sealed class SpotifyImportSource {
    abstract val stableId: String
    abstract val title: String
    abstract val trackCount: Int?
    abstract val thumbnailUrl: String?

    data class Playlist(
        val spotifyId: String,
        override val title: String,
        override val trackCount: Int?,
        override val thumbnailUrl: String?,
    ) : SpotifyImportSource() {
        override val stableId: String = "SPOTIFY_PLAYLIST_$spotifyId"
    }

    data class LikedSongs(
        override val trackCount: Int?,
    ) : SpotifyImportSource() {
        override val stableId: String = "SPOTIFY_LIKED_SONGS"
        override val title: String = "Liked Songs"
        override val thumbnailUrl: String? = null
    }
}

data class SpotifyImportProgress(
    val sourceTitle: String,
    val completedSources: Int,
    val totalSources: Int,
    val matchedTracks: Int,
    val totalTracks: Int,
)

data class SpotifyImportSourceSummary(
    val title: String,
    val totalTracks: Int,
    val importedTracks: Int,
    val failedTracks: Int,
)

data class SpotifyImportSummary(
    val sources: List<SpotifyImportSourceSummary>,
) {
    val importedTracks: Int get() = sources.sumOf { it.importedTracks }
    val failedTracks: Int get() = sources.sumOf { it.failedTracks }
    val totalTracks: Int get() = sources.sumOf { it.totalTracks }
}

sealed class SpotifyImportState {
    data class Progress(
        val progress: SpotifyImportProgress,
    ) : SpotifyImportState()

    data class Done(
        val summary: SpotifyImportSummary,
    ) : SpotifyImportState()

    data class Failed(
        val message: String,
    ) : SpotifyImportState()
}

interface SpotifyImportRepository {
    /**
     * Imports every playlist the connected Spotify account owns or follows, plus Liked Songs, as
     * local playlists — resolving each track to a real YT Music song. Re-running this refreshes
     * previously-imported playlists in place (matched via [SpotifyImportSource.stableId]) rather
     * than creating duplicates.
     */
    fun importAllPlaylists(): Flow<SpotifyImportState>
}
