package echo.music.enhanced.data.canvas

// The canvas provider modules (Tidal, Apple Music, EchoMusicCanvas, ArtistVideo)
// are Android-only. The jvm/desktop target is unused scaffolding (this fork ships
// Android only), so these just return null rather than pulling the modules in.

internal actual suspend fun fetchTidalCanvas(
    song: String,
    artist: String,
    album: String?,
): RawCanvasFetch? = null

internal actual suspend fun fetchAppleCanvas(
    song: String,
    artist: String,
    album: String?,
): RawCanvasFetch? = null

internal actual suspend fun fetchEchoMusicCanvas(
    song: String,
    artist: String,
): RawCanvasFetch? = null

internal actual suspend fun fetchArtistVideoCanvas(
    song: String,
    artist: String,
    album: String?,
    duration: Int?,
): RawCanvasFetch? = null

internal actual suspend fun fetchAppleArtistBackground(artistName: String): RawCanvasFetch? = null
