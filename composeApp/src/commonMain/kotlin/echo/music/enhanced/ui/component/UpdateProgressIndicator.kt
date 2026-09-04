package echo.music.enhanced.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive's real CircularWavyProgressIndicator for the in-app updater's download
 * progress — the wave settles toward a plain ring as the download nears completion, matching this
 * fork's established "calm down when idle/finishing" polish (the mini-player cookie, the Now
 * Playing play/pause cookie, the paused Glow Now Playing background).
 */
@Composable
fun UpdateProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        CircularWavyProgressIndicator(
            progress = { progress },
            modifier = modifier.size(size),
            amplitude = { p -> (1f - p).coerceIn(0f, 1f) },
        )
    }
}
