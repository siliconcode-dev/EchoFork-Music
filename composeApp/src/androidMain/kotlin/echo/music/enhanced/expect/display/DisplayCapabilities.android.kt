package echo.music.enhanced.expect.display

import android.os.Build
import android.view.Display
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import echo.music.enhanced.extension.getActivityOrNull
import kotlin.math.roundToInt

private fun Display.Mode.refreshRateHz(): Int = refreshRate.roundToInt()

@Composable
actual fun isHighRefreshRateSupported(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return false
    val display = LocalView.current.display ?: return false
    return display.supportedModes.any { it.refreshRateHz() > 60 }
}

@Composable
actual fun getSupportedRefreshRatesHz(): List<Int> {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return emptyList()
    val display = LocalView.current.display ?: return emptyList()
    return display.supportedModes.map { it.refreshRateHz() }.distinct().sorted()
}

@Composable
actual fun ApplyTrueMotionRefreshRate(targetHz: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return
    val context = LocalContext.current
    val view = LocalView.current
    LaunchedEffect(targetHz) {
        val window = context.getActivityOrNull()?.window ?: return@LaunchedEffect
        val display = view.display ?: return@LaunchedEffect
        val modes = display.supportedModes
        val modeId =
            when {
                targetHz < 0 -> 0 // no preference
                targetHz == 0 -> modes.maxByOrNull { it.refreshRateHz() }?.modeId ?: 0
                else -> modes.minByOrNull { kotlin.math.abs(it.refreshRateHz() - targetHz) }?.modeId ?: 0
            }
        window.attributes =
            window.attributes.apply {
                preferredDisplayModeId = modeId
            }
    }
}
