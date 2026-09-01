package echo.music.enhanced.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp

// Material Symbols "surround_sound", outlined, 24px — original viewBox "0 -960 960 960"
// shifted into a (0,0)-anchored viewport via the translationY group below.
private const val SURROUND_SOUND_PATH_DATA =
    "M480-360q50 0 85-35t35-85q0-50-35-85t-85-35q-50 0-85 35t-35 85q0 50 35 85t85 35Zm198 78q40-40 61-91t21-107q0-56-21-107t-61-91l-56 56q29 29 43.5 65.5T680-480q0 40-14.5 76.5T622-338l56 56Zm-396 0 56-56q-29-29-43.5-65.5T280-480q0-40 14.5-76.5T338-622l-56-56q-40 40-61 91t-21 107q0 56 21 107t61 91ZM160-160q-33 0-56.5-23.5T80-240v-480q0-33 23.5-56.5T160-800h640q33 0 56.5 23.5T880-720v480q0 33-23.5 56.5T800-160H160Zm0-80h640v-480H160v480Zm0 0v-480 480Z"

@Suppress("CheckReturnValue")
val echoIcons.SurroundSound: ImageVector
  get() {
    if (_SurroundSound != null) {
      return _SurroundSound!!
    }
    _SurroundSound =
      ImageVector.Builder(
          name = "SurroundSound",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 960f,
          viewportHeight = 960f,
        )
        .apply {
          group(translationY = 960f) {
            addPath(
              pathData = PathParser().parsePathString(SURROUND_SOUND_PATH_DATA).toNodes(),
              fill = SolidColor(Color.Black),
              pathFillType = PathFillType.Companion.NonZero,
            )
          }
        }
        .build()
    return _SurroundSound!!
  }

private var _SurroundSound: ImageVector? = null
