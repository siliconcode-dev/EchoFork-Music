package echo.music.enhanced.kotlinytmusicscraper.models.body

import echo.music.enhanced.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class EditPlaylistBody(
    val context: Context,
    val playlistId: String,
    val actions: List<Action>,
) {
    @Serializable
    data class Action(
        val action: String = "ACTION_SET_PLAYLIST_NAME",
        val playlistName: String? = null,
        val addedVideoId: String? = null,
        val removedVideoId: String? = null,
        val setVideoId: String? = null,
        val movedSetVideoIdSuccessor: String? = null,
    )
}