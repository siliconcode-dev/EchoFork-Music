package echo.music.enhanced.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import echo.music.enhanced.extension.artworkScrimBrush
import echo.music.enhanced.extension.greyScale
import echo.music.enhanced.ui.navigation.destination.home.AnalyticsDestination
import echo.music.enhanced.ui.navigation.destination.home.HomeDestination
import echo.music.enhanced.ui.navigation.destination.library.LibraryDestination
import echo.music.enhanced.ui.navigation.destination.search.SearchDestination
import echo.music.enhanced.ui.theme.typo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import echomusic.composeapp.generated.resources.*
import kotlin.reflect.KClass

@Composable
fun AppBottomNavigationBar(
    startDestination: Any = HomeDestination,
    navController: NavController,
    isTranslucentBackground: Boolean = false,
    showAnalyticsTab: Boolean = false,
    isBetterEcho: Boolean = false,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = { _ -> },
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    // `ordinal` identifies a tab, it is NOT the position — Analytics sits before Library here while
    // keeping the ordinal it was declared with, so that the numbering stays stable whether or not
    // the tab is present.
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
                else -> BottomNavScreen.Home.ordinal // Default to Home if not recognized
            },
        )
    }
    // Tracking can be turned off while Analytics is the selected tab, and that tab then disappears
    // from the list. Fall back to Home so nothing is left highlighted.
    LaunchedEffect(showAnalyticsTab) {
        if (!showAnalyticsTab && selectedIndex == BottomNavScreen.Analytics.ordinal) {
            selectedIndex = BottomNavScreen.Home.ordinal
        }
    }
    val navBarContent: @Composable () -> Unit = {
        NavigationBar(
            windowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
        ) {
            bottomNavScreens.forEach { screen ->
                NavigationBarItem(
                    selected = selectedIndex == screen.ordinal,
                    onClick = {
                        if (selectedIndex == screen.ordinal) {
                            if (currentBackStackEntry?.destination?.hierarchy?.any {
                                    it.hasRoute(screen.destination::class)
                                } == true
                            ) {
                                reloadDestinationIfNeeded(
                                    screen.destination::class,
                                )
                            } else {
                                navController.navigate(screen.destination)
                            }
                        } else {
                            selectedIndex = screen.ordinal
                            navController.navigate(screen.destination) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    label = {
                        Text(
                            stringResource(screen.title),
                            style =
                                if (selectedIndex == screen.ordinal) {
                                    typo().bodySmall
                                } else {
                                    typo().bodySmall.greyScale()
                                },
                        )
                    },
                    icon = screen.icon,
                    colors =
                        NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    modifier =
                        Modifier.windowInsetsPadding(
                            NavigationBarDefaults.windowInsets,
                        ),
                )
            }
        }
    }
    // Better Echo: a floating rounded dock (same 24dp corner token as RowGroupCard) instead of
    // Classic's edge-to-edge flat bar — visually distinct from both Classic and the glass dock.
    if (isBetterEcho) {
        Box(modifier = Modifier.wrapContentSize().padding(horizontal = 16.dp, vertical = 4.dp)) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shadowElevation = 3.dp,
            ) {
                navBarContent()
            }
        }
    } else {
        Box(modifier = Modifier.wrapContentSize()) {
            navBarContent()
        }
    }
}

@Composable
fun AppNavigationRail(
    startDestination: Any = HomeDestination,
    navController: NavController,
    showAnalyticsTab: Boolean = false,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = { _ -> },
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    // See the note in AppBottomNavigationBar: `ordinal` is the tab's identity, not its position.
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
                else -> BottomNavScreen.Home.ordinal // Default to Home if not recognized
            },
        )
    }
    // Tracking can be turned off while Analytics is the selected tab, and that tab then disappears
    // from the list. Fall back to Home so nothing is left highlighted.
    LaunchedEffect(showAnalyticsTab) {
        if (!showAnalyticsTab && selectedIndex == BottomNavScreen.Analytics.ordinal) {
            selectedIndex = BottomNavScreen.Home.ordinal
        }
    }
    NavigationRail {
        Spacer(Modifier.height(16.dp))
        Box(Modifier.padding(horizontal = 16.dp)) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.mono),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .height(32.dp)
                            .clip(CircleShape),
                )
            }
        }
        Spacer(Modifier.weight(1f))
        bottomNavScreens.forEach { screen ->
            NavigationRailItem(
                icon = screen.icon,
                label = {
                    Text(
                        stringResource(screen.title),
                        style =
                            if (selectedIndex == screen.ordinal) {
                                typo().bodySmall
                            } else {
                                typo().bodySmall.greyScale()
                            },
                    )
                },
                selected = selectedIndex == screen.ordinal,
                onClick = {
                    if (selectedIndex == screen.ordinal) {
                        if (currentBackStackEntry?.destination?.hierarchy?.any {
                                it.hasRoute(screen.destination::class)
                            } == true
                        ) {
                            reloadDestinationIfNeeded(
                                screen.destination::class,
                            )
                        } else {
                            navController.navigate(screen.destination)
                        }
                    } else {
                        selectedIndex = screen.ordinal
                        navController.navigate(screen.destination) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}