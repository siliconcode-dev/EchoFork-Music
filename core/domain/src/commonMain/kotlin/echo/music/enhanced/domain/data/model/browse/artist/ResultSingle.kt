package echo.music.enhanced.domain.data.model.browse.artist

import echo.music.enhanced.domain.data.model.searchResult.songs.Thumbnail
import echo.music.enhanced.domain.data.type.HomeContentType

data class ResultSingle(
    val browseId: String,
    val thumbnails: List<Thumbnail>,
    val title: String,
    val year: String,
) : HomeContentType