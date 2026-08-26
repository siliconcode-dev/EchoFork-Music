package echo.music.enhanced.kotlinytmusicscraper.pages

import echo.music.enhanced.kotlinytmusicscraper.models.AlbumItem
import echo.music.enhanced.kotlinytmusicscraper.models.VideoItem

data class ExplorePage(
    val released: List<AlbumItem>,
    val musicVideo: List<VideoItem>,
)