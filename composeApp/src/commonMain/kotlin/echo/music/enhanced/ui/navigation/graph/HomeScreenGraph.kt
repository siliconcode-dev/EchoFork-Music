package echo.music.enhanced.ui.navigation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import echo.music.enhanced.ui.navigation.destination.home.AboutDestination
import echo.music.enhanced.ui.navigation.destination.home.MoodDestination
import echo.music.enhanced.ui.navigation.destination.home.RecentlySongsDestination
import echo.music.enhanced.ui.navigation.destination.home.SettingsDestination
import echo.music.enhanced.ui.screen.home.MoodScreen
import echo.music.enhanced.ui.screen.home.RecentlySongsScreen
import echo.music.enhanced.ui.screen.home.SettingScreen


fun NavGraphBuilder.homeScreenGraph(
    innerPadding: PaddingValues,
    navController: NavController,
) {
    composable<MoodDestination> { entry ->
        val params = entry.toRoute<MoodDestination>().params
        MoodScreen(
            navController = navController,
            params = params,
        )
    }

    composable<RecentlySongsDestination> {
        RecentlySongsScreen(
            navController = navController,
            innerPadding = innerPadding,
        )
    }
    composable<SettingsDestination> { entry ->
        val args = entry.toRoute<SettingsDestination>()
        SettingScreen(
            navController = navController,
            innerPadding = innerPadding,
            highlightSection = args.highlightSection,
        )
    }
    composable<echo.music.enhanced.ui.navigation.destination.home.EqualizerDestination> {
        echo.music.enhanced.ui.screen.home.EqualizerScreen(
            navController = navController,
            innerPadding = innerPadding,
        )
    }
    composable<AboutDestination> {
        echo.music.enhanced.ui.screen.home.AboutScreen(
            navController = navController,
            innerPadding = innerPadding,
        )
    }

}