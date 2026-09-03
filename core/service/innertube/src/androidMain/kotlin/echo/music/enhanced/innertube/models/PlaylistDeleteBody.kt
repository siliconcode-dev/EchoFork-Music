package echo.music.enhanced.innertube.models.body

import echo.music.enhanced.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDeleteBody(
    val context: Context,
    val playlistId: String
)
