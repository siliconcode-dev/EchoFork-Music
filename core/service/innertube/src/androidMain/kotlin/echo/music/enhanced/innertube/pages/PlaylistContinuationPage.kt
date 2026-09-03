package echo.music.enhanced.innertube.pages

import echo.music.enhanced.innertube.models.SongItem

data class PlaylistContinuationPage(
    val songs: List<SongItem>,
    val continuation: String?,
)
