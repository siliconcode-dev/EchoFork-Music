package echo.music.enhanced.domain.repository

import echo.music.enhanced.domain.utils.LocalResource
import kotlinx.coroutines.flow.Flow

interface AiPlaylistRepository {
    /** Generates a new local playlist from [prompt]. Emits the new playlist's id. */
    fun generatePlaylist(
        prompt: String,
        songCount: Int,
    ): Flow<LocalResource<Long>>

    /** Adds/removes songs on an existing local playlist per [prompt]. */
    fun modifyPlaylist(
        playlistId: Long,
        prompt: String,
    ): Flow<LocalResource<String>>

    /** Creates or refreshes the auto-generated "Recommended by AI" playlist. Emits its id. */
    fun generateRecommendations(songCount: Int): Flow<LocalResource<Long>>
}
