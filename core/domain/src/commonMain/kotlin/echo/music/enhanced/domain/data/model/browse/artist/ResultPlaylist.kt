package echo.music.enhanced.domain.data.model.browse.artist

import echo.music.enhanced.domain.data.model.searchResult.songs.Thumbnail
import echo.music.enhanced.domain.data.type.HomeContentType

data class ResultPlaylist(
    val id: String,
    val author: String,
    val thumbnails: List<Thumbnail>,
    val title: String,
) : HomeContentType