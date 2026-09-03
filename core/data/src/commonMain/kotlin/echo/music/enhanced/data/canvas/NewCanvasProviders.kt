package echo.music.enhanced.data.canvas

/**
 * Bridge to the newly-vendored canvas provider modules (Tidal/Apple/EchoMusic
 * per-song canvas, ArtistVideo, and the Apple Music artist-background video),
 * which are Android-only (no commonMain/jvm target). Their DTOs (`CanvasArtwork`,
 * `ArtistVideoResponse`) live in those same Android-only modules, so this bridge
 * maps to a plain commonMain-visible result instead of exposing them directly.
 */
internal data class RawCanvasFetch(
    val url: String,
    val isVideo: Boolean,
    val thumbUrl: String?,
)

internal expect suspend fun fetchTidalCanvas(
    song: String,
    artist: String,
    album: String?,
): RawCanvasFetch?

internal expect suspend fun fetchAppleCanvas(
    song: String,
    artist: String,
    album: String?,
): RawCanvasFetch?

internal expect suspend fun fetchEchoMusicCanvas(
    song: String,
    artist: String,
): RawCanvasFetch?

internal expect suspend fun fetchArtistVideoCanvas(
    song: String,
    artist: String,
    album: String?,
    duration: Int?,
): RawCanvasFetch?

/** Artist-level backdrop video (not per-song) — always a video, no thumbnail. */
internal expect suspend fun fetchAppleArtistBackground(artistName: String): RawCanvasFetch?
