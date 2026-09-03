package echo.music.enhanced.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.expect.ui.PlatformBackdrop

/**
 * Upstream Echo Music's real settings-list shape language (`ui/component/Material3SettingsGroup.kt`,
 * fetched from EchoMusicApp/Echo-Music's actual current source into `upstream-latest/` for direct
 * reference — not reconstructed from a summary): each row in [items] is its own [Card] with a
 * 24dp corner at the group's outer ends and a 4dp corner where rows meet, separated by 2dp gaps —
 * a "squircle stack" rather than one flat wrapping card, with `animateContentSize()` on each row
 * matching upstream exactly. Optional [itemStates] add upstream's "highlighted" background
 * (e.g. a search-result scroll target) — the matching icon-tint/badge treatment for an item's own
 * icon lives on [SettingItem] itself (its `isHighlighted`/`showBadge` params), since [items] here
 * are opaque composables this wrapper doesn't reach inside of.
 *
 * Renders three ways depending on [interfaceMode]: Classic is a plain passthrough [Column] (zero
 * visual change from today, upstream has no such mode); Better Echo renders the opaque squircle
 * stack, upstream's actual current design; Liquid Glass renders the same shapes via [liquidGlass]
 * instead of a flat color, using the caller's own [backdrop] — every Liquid Glass call site must
 * supply one from its own local `rememberBackdrop`/`layerBackdrop` pair (see
 * [echo.music.enhanced.expect.ui.rememberBackdrop]); if none is supplied, Liquid Glass falls back
 * to the same passthrough Classic uses rather than crashing.
 */
data class GroupItemState(
    val isHighlighted: Boolean = false,
)

@Composable
fun Material3SettingsGroup(
    interfaceMode: String,
    items: List<@Composable () -> Unit>,
    modifier: Modifier = Modifier,
    backdrop: PlatformBackdrop? = null,
    itemStates: List<GroupItemState> = emptyList(),
) {
    when {
        interfaceMode == DataStoreManager.INTERFACE_BETTER_ECHO ->
            Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items.forEachIndexed { index, item ->
                    val isHighlighted = itemStates.getOrNull(index)?.isHighlighted ?: false
                    GroupRow(
                        shape = rowGroupShape(index, items.size),
                        containerColor =
                            if (isHighlighted) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceContainerHigh
                            },
                        modifier = Modifier.fillMaxWidth(),
                        content = item,
                    )
                }
            }

        interfaceMode == DataStoreManager.INTERFACE_LIQUID_GLASS && backdrop != null ->
            Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                items.forEachIndexed { index, item ->
                    val shape = rowGroupShape(index, items.size)
                    GroupRow(
                        shape = shape,
                        containerColor = Color.Transparent,
                        modifier = Modifier.fillMaxWidth().liquidGlass(backdrop = backdrop, shape = shape, interactive = false),
                        content = item,
                    )
                }
            }

        else ->
            Column(modifier.fillMaxWidth()) {
                items.forEach { it() }
            }
    }
}

@Composable
private fun GroupRow(
    shape: Shape,
    containerColor: Color,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.animateContentSize(),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        content()
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
