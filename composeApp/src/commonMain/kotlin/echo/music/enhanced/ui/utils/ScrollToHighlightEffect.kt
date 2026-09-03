package echo.music.enhanced.ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

/**
 * Ported from upstream Echo Music's `ui/utils/ScrollToHighlightEffect.kt` (fetched into
 * `upstream-latest/` for direct reference) — auto-scrolls a Settings search result into view.
 * Upstream's Settings screen is a plain scrollable `Column` (`Modifier.scrollToOnHighlight`,
 * pixel-position math against a `ScrollState`); this fork's `SettingScreen.kt` is a `LazyColumn`,
 * so the equivalent here targets a `LazyListState` item index directly via
 * `animateScrollToItem` — simpler than upstream's pixel math, and the natural fit for the
 * container type actually in use here.
 */
@Composable
fun ScrollToHighlightEffect(
    listState: LazyListState,
    highlightedItemIndex: Int?,
    delayMs: Long = 300L,
) {
    LaunchedEffect(highlightedItemIndex) {
        if (highlightedItemIndex != null) {
            delay(delayMs)
            listState.animateScrollToItem(highlightedItemIndex)
        }
    }
}
