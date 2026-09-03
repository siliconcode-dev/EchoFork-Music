package echo.music.enhanced.data.lyrics

// The 4 new lyrics provider modules (YouLyPlus, Paxsenix, KuGou, Unison) are
// Android-only. The jvm/desktop target is unused scaffolding (this fork ships
// Android only), so these just fail rather than pulling the modules in.

internal actual suspend fun fetchYouLyPlusLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String> = Result.failure(UnsupportedOperationException("YouLyPlus lyrics are Android-only"))

internal actual suspend fun fetchPaxsenixLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String> = Result.failure(UnsupportedOperationException("Paxsenix lyrics are Android-only"))

internal actual suspend fun fetchKuGouLyrics(
    title: String,
    artist: String,
    duration: Int,
    album: String?,
): Result<String> = Result.failure(UnsupportedOperationException("KuGou lyrics are Android-only"))

internal actual suspend fun fetchUnisonLyrics(
    videoId: String?,
    title: String,
    artist: String,
    album: String?,
    durationSeconds: Int,
): Result<String> = Result.failure(UnsupportedOperationException("Unison lyrics are Android-only"))
