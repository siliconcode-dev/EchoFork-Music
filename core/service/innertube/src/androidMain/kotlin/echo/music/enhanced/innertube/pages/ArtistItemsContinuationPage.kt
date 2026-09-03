package echo.music.enhanced.innertube.pages

import echo.music.enhanced.innertube.models.YTItem

data class ArtistItemsContinuationPage(
    val items: List<YTItem>,
    val continuation: String?,
)
