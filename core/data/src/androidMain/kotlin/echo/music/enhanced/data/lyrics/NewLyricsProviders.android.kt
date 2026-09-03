package echo.music.enhanced.data.lyrics

import echo.music.enhanced.kugou.KuGou
import echo.music.enhanced.paxsenixlyrics.Paxsenix
import echo.music.enhanced.unison.Unison
import echo.music.enhanced.youlyplus.YouLyPlus

internal actual suspend fun fetchYouLyPlusLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String> = YouLyPlus.getLyrics(title = title, artist = artist, duration = duration, album = album)

internal actual suspend fun fetchPaxsenixLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String> = Paxsenix.getLyrics(title = title, artist = artist, duration = duration, album = album)

internal actual suspend fun fetchKuGouLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String> = KuGou.getLyrics(title = title, artist = artist, duration = duration, album = album)

internal actual suspend fun fetchUnisonLyrics(
    videoId: String?,
    title: String,
    artist: String,
    album: String?,
    durationSeconds: Int,
): Result<String> =
    Unison.getLyrics(videoId = videoId, title = title, artist = artist, album = album, durationSeconds = durationSeconds)
