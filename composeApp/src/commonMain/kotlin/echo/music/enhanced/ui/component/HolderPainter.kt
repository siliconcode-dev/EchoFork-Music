package echo.music.enhanced.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import echo.music.enhanced.ui.theme.LocalAppColors

/**
 * Theme-aware artwork placeholder.
 */
@Composable
fun rememberHolderPainter(isVideo: Boolean = false): Painter {
    return ColorPainter(LocalAppColors.current.shimmerBackground)
}
