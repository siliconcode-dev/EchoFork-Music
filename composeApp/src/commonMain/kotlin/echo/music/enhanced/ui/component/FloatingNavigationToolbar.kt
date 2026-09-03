@file:OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class, androidx.compose.material3.ExperimentalMaterial3Api::class)

package echo.music.enhanced.ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableIntStateOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.ui.icon.MoreHoriz
import echo.music.enhanced.ui.icon.Shuffle
import echo.music.enhanced.ui.icon.Sparks
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.navigation.destination.home.AnalyticsDestination
import echo.music.enhanced.ui.navigation.destination.home.HomeDestination
import echo.music.enhanced.ui.navigation.destination.library.LibraryDestination
import echo.music.enhanced.ui.navigation.destination.search.SearchDestination
import echo.music.enhanced.ui.theme.typo
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.ai
import echomusic.composeapp.generated.resources.more_label
import echomusic.composeapp.generated.resources.more_options
import echomusic.composeapp.generated.resources.shuffle
import kotlin.reflect.KClass
import org.jetbrains.compose.resources.stringResource

/**
 * Better Echo's default nav bar, ported from upstream Echo Music's real current
 * `FloatingNavigationToolbar.kt` (fetched into `upstream-latest/` for direct reference): a
 * Material3-Expressive [HorizontalFloatingToolbar] with a sliding selection pill, plus an overflow
 * FAB opening a "More Options" sheet (Shuffle + AI Hub). Adapted to this fork's [BottomNavScreen]
 * (a single icon per tab rather than upstream's active/inactive icon pair — the tint color change
 * already carries the selected signal), this fork's own `Material3SettingsGroup`/`SettingItem` row
 * primitives instead of upstream's `Material3SettingsItem` data class, and — since upstream manages
 * nav-selection state globally in `MainActivity.kt` while this fork's existing
 * `AppBottomNavigationBar` self-manages it — the same self-contained
 * startDestination/navController/reloadDestinationIfNeeded shape as that existing component, so
 * the `App.kt` call site is a drop-in swap.
 */
@Composable
fun FloatingNavigationToolbar(
    startDestination: Any = HomeDestination,
    navController: NavController,
    showAnalyticsTab: Boolean = false,
    onShuffleClick: () -> Unit,
    shuffleEnabled: Boolean,
    onAiHubClick: () -> Unit,
    modifier: Modifier = Modifier,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = { _ -> },
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomNavScreens =
        listOfNotNull(
            BottomNavScreen.Home,
            BottomNavScreen.Search,
            BottomNavScreen.Analytics.takeIf { showAnalyticsTab },
            BottomNavScreen.Library,
        )
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(
            when (startDestination) {
                is HomeDestination -> BottomNavScreen.Home.ordinal
                is SearchDestination -> BottomNavScreen.Search.ordinal
                is LibraryDestination -> BottomNavScreen.Library.ordinal
                is AnalyticsDestination -> BottomNavScreen.Analytics.ordinal
                else -> BottomNavScreen.Home.ordinal
            },
        )
    }
    LaunchedEffect(showAnalyticsTab) {
        if (!showAnalyticsTab && selectedIndex == BottomNavScreen.Analytics.ordinal) {
            selectedIndex = BottomNavScreen.Home.ordinal
        }
    }

    FloatingNavigationToolbarContent(
        items = bottomNavScreens,
        isSelected = { it.ordinal == selectedIndex },
        onItemClick = { screen ->
            if (selectedIndex == screen.ordinal) {
                if (currentBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(screen.destination::class) } == true) {
                    reloadDestinationIfNeeded(screen.destination::class)
                } else {
                    navController.navigate(screen.destination)
                }
            } else {
                selectedIndex = screen.ordinal
                navController.navigate(screen.destination) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
        onShuffleClick = onShuffleClick,
        shuffleEnabled = shuffleEnabled,
        onAiHubClick = onAiHubClick,
        modifier = modifier,
    )
}

@Composable
private fun FloatingNavigationToolbarContent(
    items: List<BottomNavScreen>,
    isSelected: (BottomNavScreen) -> Boolean,
    onItemClick: (BottomNavScreen) -> Unit,
    onShuffleClick: () -> Unit,
    shuffleEnabled: Boolean,
    onAiHubClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSheet by rememberSaveable { mutableStateOf(false) }

    BoxWithConstraints(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        HorizontalFloatingToolbar(
            expanded = true,
            floatingActionButton = {
                FloatingToolbarDefaults.VibrantFloatingActionButton(
                    onClick = { showSheet = true },
                    shape = CircleShape,
                ) {
                    Icon(imageVector = echoIcons.MoreHoriz, contentDescription = stringResource(Res.string.more_label))
                }
            },
            modifier = Modifier.widthIn(max = 480.dp),
            colors = FloatingToolbarDefaults.standardFloatingToolbarColors(),
        ) {
            ToolbarItemsContainer(items = items, isSelected = isSelected, onItemClick = onItemClick)
        }
    }

    if (showSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(Res.string.more_options),
                    style = typo().headlineSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                )
                Material3SettingsGroup(
                    interfaceMode = DataStoreManager.INTERFACE_BETTER_ECHO,
                    items =
                        listOf<@Composable () -> Unit>(
                            {
                                SettingItem(
                                    title = stringResource(Res.string.shuffle),
                                    subtitle = "",
                                    icon = echoIcons.Shuffle,
                                    switch = shuffleEnabled to { onShuffleClick() },
                                    onClick = onShuffleClick,
                                )
                            },
                            {
                                SettingItem(
                                    title = stringResource(Res.string.ai),
                                    subtitle = "",
                                    icon = echoIcons.Sparks,
                                    onClick = {
                                        showSheet = false
                                        onAiHubClick()
                                    },
                                )
                            },
                        ),
                )
            }
        }
    }
}

@Composable
private fun ToolbarItemsContainer(
    items: List<BottomNavScreen>,
    isSelected: (BottomNavScreen) -> Boolean,
    onItemClick: (BottomNavScreen) -> Unit,
) {
    val density = LocalDensity.current
    val itemWidths = remember { mutableStateMapOf<BottomNavScreen, Dp>() }
    val itemPositions = remember { mutableStateMapOf<BottomNavScreen, Dp>() }

    val activeScreen = items.find { isSelected(it) }
    val targetWidth = itemWidths[activeScreen] ?: 0.dp
    val targetPosition = itemPositions[activeScreen] ?: 0.dp

    val slidingPillWidth by
        animateDpAsState(
            targetValue = targetWidth,
            animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
            label = "pillWidth",
        )
    val slidingPillOffset by
        animateDpAsState(
            targetValue = targetPosition,
            animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
            label = "pillOffset",
        )

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Box(modifier = Modifier.matchParentSize()) {
            if (targetWidth > 0.dp) {
                Box(
                    modifier =
                        Modifier
                            .offset(x = slidingPillOffset)
                            .width(slidingPillWidth)
                            .fillMaxHeight()
                            .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(24.dp)),
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            items.forEach { screen ->
                val selected = isSelected(screen)
                FloatingNavigationToolbarItem(
                    screen = screen,
                    selected = selected,
                    onClick = { onItemClick(screen) },
                    modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                            itemWidths[screen] = with(density) { coordinates.size.width.toDp() }
                            itemPositions[screen] = with(density) { coordinates.positionInParent().x.toDp() }
                        },
                )
            }
        }
    }
}

@Composable
private fun FloatingNavigationToolbarItem(
    screen: BottomNavScreen,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor =
        if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by
        animateFloatAsState(
            targetValue = if (isPressed) 0.91f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
            label = "pressScale",
        )

    Row(
        modifier =
            modifier
                .scale(pressScale)
                .clip(RoundedCornerShape(24.dp))
                .clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = onClick,
                ).widthIn(min = 48.dp)
                .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            screen.icon()
        }
    }
}
