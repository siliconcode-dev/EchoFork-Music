package echo.music.enhanced.data.canvas

import echo.music.enhanced.applecanvas.AppleMusicCanvasProvider
import echo.music.enhanced.artistvideo.ArtistVideoCanvasProvider
import echo.music.enhanced.artistvideo.ArtistVideoResponse
import echo.music.enhanced.canvas.AppleMusicArtistBackgroundProvider
import echo.music.enhanced.canvas.CanvasArtwork
import echo.music.enhanced.canvas.TidalCanvasProvider
import echo.music.enhanced.echomusiccanvas.EchoMusicCanvasProvider

private fun CanvasArtwork.toRawCanvasFetch(): RawCanvasFetch? {
    val url = preferredAnimationUrl ?: static ?: return null
    return RawCanvasFetch(url = url, isVideo = preferredAnimationUrl != null, thumbUrl = null)
}

private fun ArtistVideoResponse.toRawCanvasFetch(): RawCanvasFetch? {
    val url = preferredAnimationUrl ?: static ?: return null
    return RawCanvasFetch(url = url, isVideo = preferredAnimationUrl != null, thumbUrl = null)
}

internal actual suspend fun fetchTidalCanvas(
    song: String,
    artist: String,
    album: String?,
): RawCanvasFetch? = TidalCanvasProvider.getBySongArtist(song = song, artist = artist, album = album)?.toRawCanvasFetch()

internal actual suspend fun fetchAppleCanvas(
    song: String,
    artist: String,
    album: String?,
): RawCanvasFetch? = AppleMusicCanvasProvider.getBySongArtist(song = song, artist = artist, album = album)?.toRawCanvasFetch()

internal actual suspend fun fetchEchoMusicCanvas(
    song: String,
    artist: String,
): RawCanvasFetch? = EchoMusicCanvasProvider.getBySongArtist(song = song, artist = artist)?.toRawCanvasFetch()

internal actual suspend fun fetchArtistVideoCanvas(
    song: String,
    artist: String,
    album: String?,
    duration: Int?,
): RawCanvasFetch? =
    ArtistVideoCanvasProvider
        .getBySongArtist(song = song, artist = artist, album = album, duration = duration)
        ?.toRawCanvasFetch()

internal actual suspend fun fetchAppleArtistBackground(artistName: String): RawCanvasFetch? =
    AppleMusicArtistBackgroundProvider.getByArtistName(artistName = artistName)?.let { url ->
        RawCanvasFetch(url = url, isVideo = true, thumbUrl = null)
    }
