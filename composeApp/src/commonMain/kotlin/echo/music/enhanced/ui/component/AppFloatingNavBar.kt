package echo.music.enhanced.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.Text
import echo.music.enhanced.ui.navigation.destination.home.AnalyticsDestination
import echo.music.enhanced.ui.navigation.destination.home.HomeDestination
import echo.music.enhanced.ui.navigation.destination.library.LibraryDestination
import echo.music.enhanced.ui.navigation.destination.search.SearchDestination
import echo.music.enhanced.ui.component.floatingtabbar.FloatingTabBar
import echo.music.enhanced.viewModel.SharedViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.reflect.KClass

/**
 * Better Echo's alternative "iOS 26 style" nav bar, ported from upstream Echo Music's own
 * `AppFloatingNavBar.kt` (fetched into `upstream-latest/` for direct reference), which calls a
 * `FloatingTabBar` composable wrapping the "compose-floating-tab-bar" library by Elyes Mansour.
 *
 * v0.1.11.2: switched from the published `io.github.elyesmansour:floatingTabBar:1.0.1` Maven
 * binary to a vendored copy of its source (`ui/component/floatingtabbar/FloatingTabBar.kt`) —
 * the binary AAR was compiled against an old `androidx.compose.animation` and threw
 * `NoSuchMethodError` on `SharedTransitionScope.sharedElement` under this project's actual
 * resolved Compose version. Upstream hit and fixed the exact same issue the exact same way
 * (confirmed via its own vendored copy's header comment) — compiling the source directly keeps
 * it binary-compatible with whatever Compose/animation version this project resolves, rather than
 * chasing a moving version-alignment target against a stale prebuilt artifact.
 *
 * Simplification vs. upstream, both disclosed and documented rather than silent: (1) this port
 * renders the bar always in its expanded state rather than wiring a scroll-collapse connection
 * through every screen's scrollable content (a much larger, cross-cutting change); (2) the docked
 * `FloatingMiniPlayer` accessory (now-playing controls built into the bar) is not ported this
 * round — this fork's existing `MiniPlayer.kt` has substantial glass/luminance-sampling state that
 * would need real design work to dock safely, left for a follow-up rather than rushed. Upstream's
 * own glass effect system (`LocalGlassEffectConfig`) is skipped too, since this mode is Better
 * Echo, not this fork's separate Liquid Glass Interface mode.
 */
@Composable
fun AppFloatingNavBar(
    startDestination: Any = HomeDestination,
    navController: NavController,
    showAnalyticsTab: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel = koinInject(),
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = { _ -> },
) {
    // Crash-safe auto-fallback (v0.1.11.2): armed for as long as this composable is on screen,
    // so a crash anywhere while the iOS-pill nav is visible forces the next launch onto the
    // reliable default nav instead — see NavCrashRecovery (androidApp) for the actual mechanism.
    DisposableEffect(Unit) {
        viewModel.setArmedIosPillNav(true)
        onDispose { viewModel.setArmedIosPillNav(false) }
    }

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

    fun onScreenClick(screen: BottomNavScreen) {
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
    }

    val selectedContentColor = MaterialTheme.colorScheme.primary
    val unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    val tabBarContentModifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(percent = 50))

    val tabScreens = bottomNavScreens.filter { it != BottomNavScreen.Search }
    val searchScreen = bottomNavScreens.firstOrNull { it == BottomNavScreen.Search }
    val selectedTabKey = bottomNavScreens.firstOrNull { it.ordinal == selectedIndex }?.ordinal?.toString() ?: BottomNavScreen.Home.ordinal.toString()

    FloatingTabBar(
        isInline = false,
        selectedTabKey = selectedTabKey,
        modifier = modifier,
        tabBarContentModifier = tabBarContentModifier,
    ) {
        tabScreens.forEach { screen ->
            val selected = screen.ordinal == selectedIndex
            tab(
                key = screen.ordinal.toString(),
                title = {
                    Text(
                        text = stringResource(screen.title),
                        color = if (selected) selectedContentColor else unselectedContentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                icon = {
                    CompositionLocalProvider(LocalContentColor provides if (selected) selectedContentColor else unselectedContentColor) {
                        screen.icon()
                    }
                },
                onClick = { onScreenClick(screen) },
            )
        }
        searchScreen?.let { screen ->
            val selected = screen.ordinal == selectedIndex
            standaloneTab(
                key = screen.ordinal.toString(),
                icon = {
                    CompositionLocalProvider(LocalContentColor provides if (selected) selectedContentColor else unselectedContentColor) {
                        screen.icon()
                    }
                },
                onClick = { onScreenClick(screen) },
            )
        }
    }
}
