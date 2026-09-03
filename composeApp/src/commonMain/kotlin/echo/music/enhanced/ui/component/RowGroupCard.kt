package echo.music.enhanced.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.expect.ui.PlatformBackdrop

/**
 * Upstream Echo Music's real settings-list shape language (`Material3SettingsGroup.kt`), ported
 * as a shared primitive: each row in [items] is its own [Card] with a 24dp corner at the group's
 * outer ends and a 4dp corner where rows meet, separated by 2dp gaps — a "squircle stack" rather
 * than one flat wrapping card.
 *
 * Renders three ways depending on [interfaceMode]: Classic is a plain passthrough [Column] (zero
 * visual change from today); Better Echo renders the opaque squircle stack; Liquid Glass renders
 * the same shapes via [liquidGlass] instead of a flat color, using the caller's own [backdrop] —
 * every Liquid Glass call site must supply one from its own local `rememberBackdrop`/
 * `layerBackdrop` pair (see [echo.music.enhanced.expect.ui.rememberBackdrop]); if none is
 * supplied, Liquid Glass falls back to the same passthrough Classic uses rather than crashing.
 */
@Composable
fun RowGroupCard(
    interfaceMode: String,
    items: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    backdrop: PlatformBackdrop? = null,
) {
    when {
        interfaceMode == DataStoreManager.INTERFACE_BETTER_ECHO ->
            Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items.forEachIndexed { index, item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = rowGroupShape(index, items.size),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    ) {
                        item()
                    }
                }
            }

        interfaceMode == DataStoreManager.INTERFACE_LIQUID_GLASS && backdrop != null ->
            Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items.forEachIndexed { index, item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().liquidGlass(backdrop = backdrop, shape = rowGroupShape(index, items.size), interactive = false),
                        shape = rowGroupShape(index, items.size),
                        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    ) {
                        item()
                    }
                }
            }

        else ->
            Column(modifier.fillMaxWidth()) {
                items.forEach { it() }
            }
    }
}

private fun rowGroupShape(
    index: Int,
    count: Int,
): Shape =
    when {
        count == 1 -> RoundedCornerShape(24.dp)
        index == 0 -> RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
        index == count - 1 -> RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 24.dp, bottomEnd = 24.dp)
        else -> RoundedCornerShape(4.dp)
    }
