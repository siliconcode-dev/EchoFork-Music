package echo.music.enhanced.domain.data.model.browse.playlist

import echo.music.enhanced.domain.data.model.searchResult.songs.Artist
import echo.music.enhanced.domain.data.model.searchResult.songs.Thumbnail

data class TrackPlaylist(
    val albumPlaylist: AlbumPlaylist,
    val artistPlaylists: List<Artist>?,
    val duration: String,
    val durationSeconds: Int,
    val isAvailable: Boolean,
    val isExplicit: Boolean,
    val likeStatus: String,
    val thumbnails: List<Thumbnail>,
    val title: String,
    val videoId: String,
    val videoType: String,
)