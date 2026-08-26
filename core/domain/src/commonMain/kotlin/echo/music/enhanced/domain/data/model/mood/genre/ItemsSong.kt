package echo.music.enhanced.domain.data.model.mood.genre

import echo.music.enhanced.domain.data.model.searchResult.songs.Artist

data class ItemsSong(
    val title: String,
    val artist: List<Artist>?,
    val videoId: String,
)