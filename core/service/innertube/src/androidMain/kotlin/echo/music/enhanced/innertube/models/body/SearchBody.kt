package echo.music.enhanced.innertube.models.body

import echo.music.enhanced.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class SearchBody(
    val context: Context,
    val query: String?,
    val params: String?,
)
