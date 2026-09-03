package echo.music.enhanced.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Ported from upstream Echo Music's res/drawable/more_horiz.xml (fetched into upstream-latest/ for
// direct reference) — the nav overflow FAB icon on FloatingNavigationToolbar. Translated 1:1 from
// the Android VectorDrawable pathData (M/Q commands only).
@Suppress("CheckReturnValue")
val echoIcons.MoreHoriz: ImageVector
  get() {
    if (_MoreHoriz != null) {
      return _MoreHoriz!!
    }
    _MoreHoriz =
      ImageVector.Builder(
          name = "MoreHoriz",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 960f,
          viewportHeight = 960f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(240f, 560f)
            quadTo(207f, 560f, 183.5f, 536.5f)
            quadTo(160f, 513f, 160f, 480f)
            quadTo(160f, 447f, 183.5f, 423.5f)
            quadTo(207f, 400f, 240f, 400f)
            quadTo(273f, 400f, 296.5f, 423.5f)
            quadTo(320f, 447f, 320f, 480f)
            quadTo(320f, 513f, 296.5f, 536.5f)
            quadTo(273f, 560f, 240f, 560f)
            close()
            moveTo(480f, 560f)
            quadTo(447f, 560f, 423.5f, 536.5f)
            quadTo(400f, 513f, 400f, 480f)
            quadTo(400f, 447f, 423.5f, 423.5f)
            quadTo(447f, 400f, 480f, 400f)
            quadTo(513f, 400f, 536.5f, 423.5f)
            quadTo(560f, 447f, 560f, 480f)
            quadTo(560f, 513f, 536.5f, 536.5f)
            quadTo(513f, 560f, 480f, 560f)
            close()
            moveTo(720f, 560f)
            quadTo(687f, 560f, 663.5f, 536.5f)
            quadTo(640f, 513f, 640f, 480f)
            quadTo(640f, 447f, 663.5f, 423.5f)
            quadTo(687f, 400f, 720f, 400f)
            quadTo(753f, 400f, 776.5f, 423.5f)
            quadTo(800f, 447f, 800f, 480f)
            quadTo(800f, 513f, 776.5f, 536.5f)
            quadTo(753f, 560f, 720f, 560f)
            close()
          }
        }
        .build()
    return _MoreHoriz!!
  }

private var _MoreHoriz: ImageVector? = null
