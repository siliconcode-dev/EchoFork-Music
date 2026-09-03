package echo.music.enhanced.innertube.pages

import echo.music.enhanced.innertube.models.YTItem

data class LibraryContinuationPage(
    val items: List<YTItem>,
    val continuation: String?,
)
