package echo.music.enhanced.data.lyrics

/**
 * Bridge to the 4 newly-vendored lyrics provider modules (YouLyPlus, Paxsenix,
 * KuGou, Unison), which are Android-only (no commonMain/jvm target). Each
 * returns raw LRC-format text on success, mapped to the domain `Lyrics` model
 * by the caller via `parseSyncedLyrics(...).toLyrics()`.
 */
internal expect suspend fun fetchYouLyPlusLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String>

internal expect suspend fun fetchPaxsenixLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String>

internal expect suspend fun fetchKuGouLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String>

internal expect suspend fun fetchUnisonLyrics(
    videoId: String?,
    title: String,
    artist: String,
    album: String?,
    durationSeconds: Int,
): Result<String>
