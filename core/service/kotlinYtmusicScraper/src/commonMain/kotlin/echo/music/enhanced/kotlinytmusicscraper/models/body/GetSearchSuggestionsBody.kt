package echo.music.enhanced.kotlinytmusicscraper.models.body

import echo.music.enhanced.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchSuggestionsBody(
    val context: Context,
    val input: String,
)