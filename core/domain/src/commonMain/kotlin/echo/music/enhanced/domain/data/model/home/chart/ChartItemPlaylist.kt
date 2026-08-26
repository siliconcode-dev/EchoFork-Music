package echo.music.enhanced.domain.data.model.home.chart

import echo.music.enhanced.domain.data.model.browse.artist.ResultPlaylist

data class ChartItemPlaylist(
    val title: String,
    val playlists: List<ResultPlaylist>,
)