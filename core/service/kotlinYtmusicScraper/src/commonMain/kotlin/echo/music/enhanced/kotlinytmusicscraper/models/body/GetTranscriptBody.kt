package echo.music.enhanced.kotlinytmusicscraper.models.body

import echo.music.enhanced.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetTranscriptBody(
    val context: Context,
    val params: String,
)