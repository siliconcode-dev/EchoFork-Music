package echo.music.enhanced.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import echo.music.enhanced.domain.extension.now
import echo.music.enhanced.ui.theme.typo
import echo.music.enhanced.utils.VersionManager
import org.jetbrains.compose.resources.stringResource
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.app_name
import echomusic.composeapp.generated.resources.version_format

@Composable
fun EndOfPage(withoutCredit: Boolean = false) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(120.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        // Text and huge spacing removed per user request
    }
}