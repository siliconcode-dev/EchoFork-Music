package echo.music.enhanced.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// Ported from upstream Echo Music's res/drawable/sparks.xml (fetched into upstream-latest/ for
// direct reference) — used for the "Create with AI" tile on the Better Echo Create Playlist
// chooser. Pure line-segment path data, translated 1:1 from the Android VectorDrawable pathData.
@Suppress("CheckReturnValue")
val echoIcons.Sparks: ImageVector
  get() {
    if (_Sparks != null) {
      return _Sparks!!
    }
    _Sparks =
      ImageVector.Builder(
          name = "Sparks",
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
            moveTo(176f, 840f)
            lineTo(120f, 784f)
            lineTo(421f, 482f)
            lineTo(240f, 437f)
            lineTo(438f, 314f)
            lineTo(421f, 80f)
            lineTo(600f, 231f)
            lineTo(816f, 143f)
            lineTo(729f, 360f)
            lineTo(880f, 538f)
            lineTo(646f, 522f)
            lineTo(522f, 720f)
            lineTo(477f, 539f)
            lineTo(176f, 840f)
            close()
            moveTo(200f, 320f)
            lineTo(120f, 240f)
            lineTo(200f, 160f)
            lineTo(280f, 240f)
            lineTo(200f, 320f)
            close()
            moveTo(555f, 517f)
            lineTo(603f, 438f)
            lineTo(696f, 445f)
            lineTo(636f, 374f)
            lineTo(671f, 288f)
            lineTo(585f, 323f)
            lineTo(514f, 264f)
            lineTo(521f, 356f)
            lineTo(442f, 405f)
            lineTo(532f, 427f)
            lineTo(555f, 517f)
            close()
            moveTo(720f, 840f)
            lineTo(640f, 760f)
            lineTo(720f, 680f)
            lineTo(800f, 760f)
            lineTo(720f, 840f)
            close()
          }
        }
        .build()
    return _Sparks!!
  }

private var _Sparks: ImageVector? = null
