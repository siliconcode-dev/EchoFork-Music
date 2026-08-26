package echo.music.enhanced.domain.data.model.mood.genre

import echo.music.enhanced.domain.data.model.searchResult.songs.Thumbnail
import echo.music.enhanced.domain.data.type.HomeContentType

data class Content(
    val playlistBrowseId: String,
    val thumbnail: List<Thumbnail>?,
    val title: Title,
) : HomeContentType