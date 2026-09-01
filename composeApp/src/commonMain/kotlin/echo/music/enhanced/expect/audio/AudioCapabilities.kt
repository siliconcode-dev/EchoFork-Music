package echo.music.enhanced.expect.audio

import androidx.compose.runtime.Composable

/**
 * Whether the platform's Spatial Audio pipeline (Android's Spatializer) is available right
 * now. Works on ordinary stereo/PCM content — no special source encoding required.
 */
@Composable
expect fun isSpatialAudioSupported(): Boolean

/**
 * Whether this device appears to ship an OEM Dolby audio-processing engine (e.g. the
 * "Dolby Atmos" toggle some Xiaomi/Samsung devices expose in system sound settings).
 *
 * There is no official cross-OEM API for this — detection is a best-effort scan of the
 * device's registered [android.media.audiofx.AudioEffect] descriptors for anything
 * Dolby-branded. Devices without such an engine (most of them) will report unsupported.
 */
@Composable
expect fun isOemDolbyEngineDetected(): Boolean
