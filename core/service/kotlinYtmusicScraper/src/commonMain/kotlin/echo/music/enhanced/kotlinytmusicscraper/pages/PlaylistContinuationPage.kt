package echo.music.enhanced.kotlinytmusicscraper.pages

import echo.music.enhanced.kotlinytmusicscraper.models.SongItem

data class PlaylistContinuationPage(
    val songs: List<SongItem>,
    val continuation: String?,
)