package echo.music.enhanced.innertube.models.body

import echo.music.enhanced.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetTranscriptBody(
    val context: Context,
    val params: String,
)
