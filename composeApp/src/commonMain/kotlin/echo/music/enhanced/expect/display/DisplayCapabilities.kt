package echo.music.enhanced.expect.display

import androidx.compose.runtime.Composable

/** Whether this device's current display offers any refresh rate above 60Hz, on Android 11+. */
@Composable
expect fun isHighRefreshRateSupported(): Boolean

/** The distinct refresh rates (Hz, rounded) this device's current display actually supports, sorted ascending. */
@Composable
expect fun getSupportedRefreshRatesHz(): List<Int>

/**
 * Applies [targetHz] as this window's preferred refresh rate for as long as the composable
 * calling this stays alive.
 * - `-1`: no preference — resets to the system default (True Motion off).
 * - `0`: match the display's highest supported rate.
 * - `>0`: a literal Hz target, snapped to the closest mode this display actually supports.
 *
 * No-op below Android 11 or when [isHighRefreshRateSupported] is false.
 */
@Composable
expect fun ApplyTrueMotionRefreshRate(targetHz: Int)
