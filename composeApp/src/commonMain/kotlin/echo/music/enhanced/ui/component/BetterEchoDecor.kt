package echo.music.enhanced.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A scalloped "cookie" badge outline (M3 Expressive's signature shape family), built by hand
 * since this app's actual Compose Multiplatform material3 dependency predates `MaterialShapes`
 * (which only ships in the newer androidx material3, not yet used here — see v0.1.8 plan notes).
 * Samples enough points around the circle that the scallops read as smooth curves at badge sizes.
 */
class ScallopedShape(
    private val bumps: Int = 12,
    private val amplitude: Float = 0.08f,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val path = Path()
        val cx = size.width / 2f
        val cy = size.height / 2f
        val baseRadius = minOf(cx, cy)
        val points = 240
        for (i in 0..points) {
            val angle = 2 * PI * i / points
            val r = baseRadius * (1f - amplitude + amplitude * cos(bumps * angle).toFloat())
            val x = cx + r * cos(angle).toFloat()
            val y = cy + r * sin(angle).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        return Outline.Generic(path)
    }
}

/** A static decorative wavy line, in the same spirit as the app's wavy playback progress indicators. */
@Composable
fun WavyDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    amplitude: Dp = 5.dp,
    wavelength: Dp = 28.dp,
    strokeWidth: Dp = 2.dp,
) {
    val density = LocalDensity.current
    Canvas(modifier = modifier.fillMaxWidth().height(amplitude * 2 + strokeWidth)) {
        val amplitudePx = with(density) { amplitude.toPx() }
        val wavelengthPx = with(density) { wavelength.toPx() }
        val strokePx = with(density) { strokeWidth.toPx() }
        val midY = size.height / 2f
        val path = Path()
        path.moveTo(0f, midY)
        var x = 0f
        var up = true
        while (x < size.width) {
            val nextX = (x + wavelengthPx / 2f).coerceAtMost(size.width)
            val cpX = x + (nextX - x) / 2f
            val direction = if (up) -1f else 1f
            path.quadraticBezierTo(cpX, midY + direction * amplitudePx, nextX, midY)
            x = nextX
            up = !up
        }
        drawPath(path = path, color = color, style = Stroke(width = strokePx, cap = StrokeCap.Round))
    }
}
