package echo.music.enhanced.ui.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kmpalette.loader.rememberNetworkLoader
import com.kmpalette.rememberDominantColorState
import echo.music.enhanced.common.CHART_SUPPORTED_COUNTRY
import echo.music.enhanced.common.Config
import echo.music.enhanced.domain.data.entities.analytics.PlaybackEventEntity
import echo.music.enhanced.domain.data.model.browse.album.Track
import echo.music.enhanced.domain.data.model.home.HomeItem
import echo.music.enhanced.domain.data.model.home.chart.Chart
import echo.music.enhanced.domain.data.model.mood.Mood
import echo.music.enhanced.domain.extension.now
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.domain.mediaservice.handler.PlaylistType
import echo.music.enhanced.domain.mediaservice.handler.QueueData
import echo.music.enhanced.domain.utils.LocalResource
import echo.music.enhanced.domain.utils.toSongEntity
import echo.music.enhanced.domain.utils.toTrack
import echo.music.enhanced.logger.Logger
import echo.music.enhanced.ui.component.Material3SettingsGroup
import echo.music.enhanced.ui.component.rememberHolderPainter
import echo.music.enhanced.extension.angledGradientBackground
import echo.music.enhanced.extension.artworkScrimBrush
import echo.music.enhanced.extension.isScrollingUp
import echo.music.enhanced.extension.rgbFactor
import echo.music.enhanced.ui.component.CenterLoadingBox
import echo.music.enhanced.ui.component.Chip
import echo.music.enhanced.ui.component.DropdownButton
import echo.music.enhanced.ui.component.EndOfPage
import echo.music.enhanced.ui.component.HomeItem
import echo.music.enhanced.ui.component.HomeItemContentPlaylist
import echo.music.enhanced.ui.component.HomeShimmer
import echo.music.enhanced.ui.component.ItemArtistChart
import echo.music.enhanced.ui.component.MoodMomentAndGenreHomeItem
import echo.music.enhanced.ui.component.OfflineErrorState
import echo.music.enhanced.ui.component.NowPlayingBottomSheet
import echo.music.enhanced.ui.component.QuickPicksItem
import echo.music.enhanced.ui.component.RippleIconButton
import echo.music.enhanced.ui.component.ShareSavedLyricsDialog
import echo.music.enhanced.ui.icon.History
import echo.music.enhanced.ui.icon.Notifications
import echo.music.enhanced.ui.icon.Settings
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.icon.Menu
import echo.music.enhanced.ui.navigation.destination.home.HomeDestination
import echo.music.enhanced.ui.navigation.destination.home.MoodDestination
import echo.music.enhanced.ui.navigation.destination.home.RecentlySongsDestination
import echo.music.enhanced.ui.navigation.destination.home.SettingsDestination
import echo.music.enhanced.ui.navigation.destination.library.LibraryDynamicPlaylistDestination
import echo.music.enhanced.ui.navigation.destination.list.ArtistDestination
import echo.music.enhanced.ui.screen.library.LibraryDynamicPlaylistType
import echo.music.enhanced.ui.navigation.destination.list.PlaylistDestination
import echo.music.enhanced.ui.navigation.destination.login.LoginDestination
import echo.music.enhanced.ui.theme.typo
import echo.music.enhanced.viewModel.HomeViewModel
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_COMMUTE
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_ENERGIZE
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_FEEL_GOOD
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_FOCUS
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_MIX_FOR_YOU
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_PARTY
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_RELAX
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_ROMANCE
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_SAD
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_SLEEP
import echo.music.enhanced.viewModel.LibraryViewModel
import echo.music.enhanced.ui.component.GridLibraryPlaylist
import echomusic.composeapp.generated.resources.mix_for_you
import echomusic.composeapp.generated.resources.no_mixes_found
import echo.music.enhanced.viewModel.HomeViewModel.Companion.HOME_PARAMS_WORKOUT
import echo.music.enhanced.viewModel.ListState
import echo.music.enhanced.viewModel.SharedViewModel
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.Url
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.all
import echomusic.composeapp.generated.resources.app_name
import echomusic.composeapp.generated.resources.cancel
import echomusic.composeapp.generated.resources.chart
import echomusic.composeapp.generated.resources.commute
import echomusic.composeapp.generated.resources.do_not_show_again
import echomusic.composeapp.generated.resources.energize
import echomusic.composeapp.generated.resources.feel_good
import echomusic.composeapp.generated.resources.focus
import echomusic.composeapp.generated.resources.go_to_log_in_page
import echomusic.composeapp.generated.resources.good_afternoon
import echomusic.composeapp.generated.resources.good_evening
import echomusic.composeapp.generated.resources.good_morning
import echomusic.composeapp.generated.resources.good_night
import echomusic.composeapp.generated.resources.let_s_pick_a_playlist_for_you
import echomusic.composeapp.generated.resources.let_s_start_with_a_radio
import echomusic.composeapp.generated.resources.log_in_warning
import echomusic.composeapp.generated.resources.party
import echomusic.composeapp.generated.resources.quick_picks
import echomusic.composeapp.generated.resources.relax
import echomusic.composeapp.generated.resources.romance
import echomusic.composeapp.generated.resources.sad
import echomusic.composeapp.generated.resources.sleep
import echomusic.composeapp.generated.resources.top_artists
import echomusic.composeapp.generated.resources.warning
import echomusic.composeapp.generated.resources.welcome_back
import echomusic.composeapp.generated.resources.what_is_best_choice_today
import echomusic.composeapp.generated.resources.workout


private val listOfHomeChip =
    listOf(
        Res.string.all,
        Res.string.mix_for_you,
        Res.string.relax,
        Res.string.sleep,
        Res.string.energize,
        Res.string.sad,
        Res.string.romance,
        Res.string.feel_good,
        Res.string.workout,
        Res.string.party,
        Res.string.commute,
        Res.string.focus,
    )

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    onScrolling: (onTop: Boolean) -> Unit = {},
    viewModel: HomeViewModel =
        koinViewModel(),
    sharedViewModel: SharedViewModel =
        koinInject(),
    libraryViewModel: LibraryViewModel =
        koinViewModel(),
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val isScrollingUp by scrollState.isScrollingUp()
    val accountInfo by viewModel.accountInfo.collectAsStateWithLifecycle()
    val homeData by viewModel.homeItemList.collectAsStateWithLifecycle()
    val newRelease by viewModel.newRelease.collectAsStateWithLifecycle()
    val chart by viewModel.chart.collectAsStateWithLifecycle()
    val moodMomentAndGenre by viewModel.exploreMoodItem.collectAsStateWithLifecycle()
    val chartLoading by viewModel.loadingChart.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val youTubeMixForYou by libraryViewModel.youTubeMixForYou.collectAsStateWithLifecycle()

    var accountShow by rememberSaveable {
        mutableStateOf(false)
    }
    val regionChart by viewModel.regionCodeChart.collectAsStateWithLifecycle()
    val reloadDestination by sharedViewModel.reloadDestination.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val chipRowState = rememberScrollState()
    val params by viewModel.params.collectAsStateWithLifecycle()
    val homeListState by viewModel.homeListState.collectAsStateWithLifecycle()
    val continuation by viewModel.continuation.collectAsStateWithLifecycle()

    val interfaceMode by sharedViewModel.getInterfaceMode().collectAsStateWithLifecycle(DataStoreManager.INTERFACE_BETTER_ECHO)
    val speedDialItems by viewModel.speedDialItems.collectAsStateWithLifecycle()
    val keepListening by viewModel.keepListening.collectAsStateWithLifecycle()
    val randomizeHomeOrder by sharedViewModel.getRandomizeHomeOrder().collectAsStateWithLifecycle(false)
    var homeRandomSeed by remember { mutableStateOf(0L) }

    val shouldShowLogInAlert by viewModel.showLogInAlert.collectAsStateWithLifecycle()

    val openAppTime by sharedViewModel.openAppTime.collectAsStateWithLifecycle()
    val shareLyricsPermissions by sharedViewModel.shareSavedLyrics.collectAsStateWithLifecycle()

    val backgroundColor = MaterialTheme.colorScheme.background
    val isLightTheme = backgroundColor.luminance() > 0.5f
    var topHeaderColor by remember {
        mutableStateOf(backgroundColor)
    }
    val animatedColor by animateColorAsState(topHeaderColor, tween(500))
    val mainHomeThumbnail by viewModel.mainHomeThumbnail.collectAsStateWithLifecycle()
    val networkLoader = rememberNetworkLoader(HttpClient(CIO))
    val dominantColorState =
        rememberDominantColorState(
            defaultColor = backgroundColor,
            defaultOnColor = backgroundColor,
            loader = networkLoader,
        )

    LaunchedEffect(mainHomeThumbnail) {
        mainHomeThumbnail?.let {
            dominantColorState.updateFrom(Url(it))
        }
    }

    LaunchedEffect(dominantColorState, isLightTheme) {
        snapshotFlow { dominantColorState.color }.collect {
            // Light theme: pull the artwork color toward white for a soft pastel header;
            // dark theme keeps the original darkened tone.
            topHeaderColor = if (isLightTheme) lerp(it, Color.White, 0.85f) else it.rgbFactor(0.3f)
        }
    }



    var topAppBarHeightPx by rememberSaveable {
        mutableIntStateOf(0)
    }

    val hazeState =
        rememberHazeState(
            blurEnabled = true,
        )

    LaunchedEffect(params) {
        if (params == HOME_PARAMS_MIX_FOR_YOU && youTubeMixForYou.data.isNullOrEmpty()) {
            libraryViewModel.getYouTubeMixedForYou()
        }
    }
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.firstVisibleItemIndex }
            .collect {
                if (it <= 1) {
                    onScrolling.invoke(true)
                } else {
                    onScrolling.invoke(isScrollingUp)
                }
            }
    }

    val onRefresh: () -> Unit = {
        isRefreshing = true
        viewModel.getHomeItemList(params)
        Logger.w("HomeScreen", "onRefresh")
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) homeRandomSeed = kotlin.random.Random.nextLong()
    }
    LaunchedEffect(key1 = reloadDestination) {
        if (reloadDestination == HomeDestination::class) {
            if (scrollState.firstVisibleItemIndex > 1) {
                Logger.w("HomeScreen", "scrollState.firstVisibleItemIndex: ${scrollState.firstVisibleItemIndex}")
                scrollState.animateScrollToItem(0)
                sharedViewModel.reloadDestinationDone()
            } else {
                Logger.w("HomeScreen", "scrollState.firstVisibleItemIndex: ${scrollState.firstVisibleItemIndex}")
                onRefresh.invoke()
            }
        }
    }
    LaunchedEffect(key1 = loading) {
        if (!loading) {
            isRefreshing = false
            sharedViewModel.reloadDestinationDone()
            coroutineScope.launch {
                pullToRefreshState.animateToHidden()
            }
        }
    }
    LaunchedEffect(key1 = homeData) {
        accountShow = homeData.find { it.subtitle == accountInfo?.first } == null
    }


    val shouldStartPaginate =
        remember {
            derivedStateOf {
                homeListState != ListState.PAGINATION_EXHAUST &&
                    (
                        scrollState.layoutInfo.visibleItemsInfo
                            .lastOrNull()
                            ?.index ?: -9
                    ) >= (scrollState.layoutInfo.totalItemsCount - 1)
            }
        }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        Logger.d("HomeScreen", "shouldStartPaginate: ${shouldStartPaginate.value}")
        Logger.d("HomeScreen", "homeListState: $homeListState")
        Logger.d("HomeScreen", "Continuation: $continuation")
        if (shouldStartPaginate.value && homeListState == ListState.IDLE) {

            viewModel.getContinueHomeItem(
                continuation,
            )
        }
    }

//    if (shouldShowGetDataSyncIdBottomSheet) {
//        GetDataSyncIdBottomSheet(
//            cookie = youTubeCookie,
//            onDismissRequest = {
//                shouldShowGetDataSyncIdBottomSheet = false
//            },
//        )
//    }





    Box {
        PullToRefreshBox(
            modifier =
                Modifier
                    .hazeSource(hazeState),
            state = pullToRefreshState,
            onRefresh = onRefresh,
            isRefreshing = isRefreshing,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    modifier =
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(
                                top =
                                    with(LocalDensity.current) {
                                        topAppBarHeightPx.toDp()
                                    },
                            ),
                    containerColor = PullToRefreshDefaults.indicatorContainerColor,
                    color = PullToRefreshDefaults.indicatorColor,
                    maxDistance = PullToRefreshDefaults.PositionalThreshold,
                )
            },
        ) {
            Crossfade(targetState = loading, label = "Home Shimmer") { loading ->
                if (!loading) {
                    if (params == HOME_PARAMS_MIX_FOR_YOU) {
                        GridLibraryPlaylist(
                            navController = navController,
                            contentPadding = PaddingValues(top = with(androidx.compose.ui.platform.LocalDensity.current) { topAppBarHeightPx.toDp() }),
                            data = youTubeMixForYou,
                            emptyText = Res.string.no_mixes_found,
                            onReload = { libraryViewModel.getYouTubeMixedForYou() }
                        )
                        return@Crossfade
                    }
                    if (homeData.isEmpty() && homeListState == ListState.PAGINATION_EXHAUST) {
                        OfflineErrorState(
                            onRetry = onRefresh,
                            onOpenDownloaded = {
                                navController.navigate(
                                    LibraryDynamicPlaylistDestination(
                                        type = LibraryDynamicPlaylistType.Downloaded.toStringParams(),
                                    ),
                                )
                            },
                        )
                        return@Crossfade
                    }
                    if (interfaceMode == DataStoreManager.INTERFACE_BETTER_ECHO) {
                        val quickPicksTitle = stringResource(Res.string.quick_picks)
                        val firstHomeItem = homeData.firstOrNull()
                        val restHomeItems = homeData.drop(1).filterNot { it.title == quickPicksTitle }
                        val keepListeningRecords = (keepListening as? LocalResource.Success)?.data ?: emptyList()
                        val frontOrder =
                            remember(randomizeHomeOrder, homeRandomSeed) {
                                if (randomizeHomeOrder) {
                                    listOf("speed_dial" to 500, "keep_listening" to 300)
                                        .map { (key, base) ->
                                            val rnd = kotlin.random.Random(homeRandomSeed + key.hashCode())
                                            key to (base + rnd.nextInt(-100, 100))
                                        }.sortedByDescending { it.second }
                                        .map { it.first }
                                } else {
                                    listOf("speed_dial", "keep_listening")
                                }
                            }
                        val tailOrder =
                            remember(randomizeHomeOrder, homeRandomSeed) {
                                if (randomizeHomeOrder) {
                                    listOf("new_release" to 100, "mood_genre" to 100, "chart" to 100)
                                        .map { (key, base) ->
                                            val rnd = kotlin.random.Random(homeRandomSeed + key.hashCode())
                                            key to (base + rnd.nextInt(-50, 50))
                                        }.sortedByDescending { it.second }
                                        .map { it.first }
                                } else {
                                    listOf("new_release", "mood_genre", "chart")
                                }
                            }
                        LazyColumn(
                            state = scrollState,
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            item(key = "be_home_header") {
                                Box {
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .height(300.dp)
                                                .angledGradientBackground(listOf(animatedColor, backgroundColor), 25f),
                                    ) {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(180.dp)
                                                    .align(Alignment.BottomCenter)
                                                    .background(artworkScrimBrush(backgroundColor)),
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.padding(horizontal = 15.dp),
                                    ) {
                                        Spacer(
                                            Modifier.height(
                                                with(LocalDensity.current) { topAppBarHeightPx.toDp() },
                                            ),
                                        )
                                        if (accountInfo != null && accountShow) {
                                            AccountLayout(
                                                accountName = accountInfo?.first ?: "",
                                                url = accountInfo?.second ?: "",
                                            )
                                            Spacer(Modifier.height(8.dp))
                                        }
                                        firstHomeItem?.let { item ->
                                            if (item.title == quickPicksTitle) {
                                                Material3SettingsGroup(
                                                    interfaceMode = interfaceMode,
                                                    items =
                                                        listOf {
                                                            QuickPicksHero(
                                                                homeItem = item,
                                                                navController = navController,
                                                                viewModel = viewModel,
                                                            )
                                                        },
                                                )
                                            } else {
                                                HomeItem(navController = navController, data = item)
                                            }
                                        }
                                    }
                                }
                            }
                            items(frontOrder, key = { it }) { key ->
                                Box(modifier = Modifier.padding(horizontal = 15.dp)) {
                                    when (key) {
                                        "speed_dial" ->
                                            if (speedDialItems.isNotEmpty()) {
                                                Material3SettingsGroup(
                                                    interfaceMode = interfaceMode,
                                                    items =
                                                        listOf {
                                                            SpeedDialSection(
                                                                items = speedDialItems,
                                                                navController = navController,
                                                                viewModel = viewModel,
                                                            )
                                                        },
                                                )
                                            }

                                        "keep_listening" ->
                                            if (keepListeningRecords.isNotEmpty()) {
                                                Material3SettingsGroup(
                                                    interfaceMode = interfaceMode,
                                                    items =
                                                        listOf {
                                                            KeepListeningSection(
                                                                records = keepListeningRecords,
                                                                navController = navController,
                                                                viewModel = viewModel,
                                                            )
                                                        },
                                                )
                                            }
                                    }
                                }
                            }
                            itemsIndexed(
                                restHomeItems,
                                key = { _, item -> item.hashCode().toString() + (mainHomeThumbnail ?: "nothumb") },
                            ) { _, item ->
                                Box(modifier = Modifier.padding(horizontal = 15.dp)) {
                                    HomeItem(navController = navController, data = item)
                                }
                            }
                            item {
                                AnimatedVisibility(
                                    homeListState == ListState.PAGINATING,
                                    enter = expandVertically() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically(),
                                ) {
                                    CenterLoadingBox(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .height(200.dp),
                                    )
                                }
                            }
                            if (homeListState == ListState.PAGINATION_EXHAUST) {
                                items(tailOrder, key = { it }) { key ->
                                    Box(modifier = Modifier.padding(horizontal = 15.dp)) {
                                        when (key) {
                                            "new_release" ->
                                                if (newRelease.isNotEmpty()) {
                                                    Material3SettingsGroup(
                                                        interfaceMode = interfaceMode,
                                                        items =
                                                            listOf {
                                                                Column {
                                                                    newRelease.forEach {
                                                                        HomeItem(navController = navController, data = it)
                                                                    }
                                                                }
                                                            },
                                                    )
                                                }

                                            "mood_genre" ->
                                                moodMomentAndGenre?.let { mood ->
                                                    Material3SettingsGroup(
                                                        interfaceMode = interfaceMode,
                                                        items =
                                                            listOf {
                                                                MoodMomentAndGenre(mood = mood, navController = navController)
                                                            },
                                                    )
                                                }

                                            "chart" ->
                                                Material3SettingsGroup(
                                                    interfaceMode = interfaceMode,
                                                    items =
                                                        listOf {
                                                            Column(
                                                                Modifier.padding(vertical = 10.dp),
                                                                verticalArrangement = Arrangement.SpaceBetween,
                                                            ) {
                                                                ChartTitle()
                                                                Spacer(modifier = Modifier.height(5.dp))
                                                                Crossfade(targetState = regionChart) {
                                                                    if (it != null) {
                                                                        DropdownButton(
                                                                            items = CHART_SUPPORTED_COUNTRY.itemsData.toList(),
                                                                            defaultSelected =
                                                                                CHART_SUPPORTED_COUNTRY.itemsData.getOrNull(
                                                                                    CHART_SUPPORTED_COUNTRY.items.indexOf(it),
                                                                                )
                                                                                    ?: CHART_SUPPORTED_COUNTRY.itemsData[1],
                                                                        ) {
                                                                            viewModel.exploreChart(
                                                                                CHART_SUPPORTED_COUNTRY.items[
                                                                                    CHART_SUPPORTED_COUNTRY.itemsData.indexOf(
                                                                                        it,
                                                                                    ),
                                                                                ],
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                                Spacer(modifier = Modifier.height(5.dp))
                                                                Crossfade(
                                                                    targetState = chartLoading,
                                                                    label = "Chart",
                                                                ) { loadingChartState ->
                                                                    if (!loadingChartState) {
                                                                        chart?.let {
                                                                            ChartData(chart = it, navController = navController)
                                                                        }
                                                                    } else {
                                                                        CenterLoadingBox(
                                                                            modifier =
                                                                                Modifier
                                                                                    .fillMaxWidth()
                                                                                    .height(400.dp),
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        },
                                                )
                                        }
                                    }
                                }
                            }
                            item {
                                EndOfPage()
                            }
                        }
                    } else {
                    LazyColumn(
                        state = scrollState,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        itemsIndexed(homeData, key = { _, item ->
                            item.hashCode().toString() + (mainHomeThumbnail ?: "nothumb")
                        }) { index, item ->
                            Box {
                                if (index == 0) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .height(300.dp)
                                                .angledGradientBackground(listOf(animatedColor, backgroundColor), 25f),
                                    ) {
                                        Box(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(180.dp)
                                                    .align(Alignment.BottomCenter)
                                                    .background(artworkScrimBrush(backgroundColor)),
                                        )
                                    }
                                }
                                Column(
                                    modifier =
                                        Modifier
                                            .padding(horizontal = 15.dp),
                                ) {
                                    if (index == 0) {
                                        Spacer(
                                            Modifier.height(
                                                with(LocalDensity.current) { topAppBarHeightPx.toDp() },
                                            ),
                                        )
                                    }
                                    if (index == 0 && accountInfo != null && accountShow) {
                                        AccountLayout(
                                            accountName = accountInfo?.first ?: "",
                                            url = accountInfo?.second ?: "",
                                        )
                                        Spacer(Modifier.height(8.dp))
                                    }
                                    if (item.title == stringResource(Res.string.quick_picks)) {
                                        AnimatedVisibility(
                                            visible =
                                                homeData.find {
                                                    it.title ==
                                                        stringResource(
                                                            Res.string.quick_picks,
                                                        )
                                                } != null,
                                        ) {
                                            QuickPicks(
                                                homeItem =
                                                    (
                                                        homeData.find {
                                                            it.title ==
                                                                stringResource(
                                                                    Res.string.quick_picks,
                                                                )
                                                        } ?: return@AnimatedVisibility
                                                    ).let { content ->
                                                        content.copy(
                                                            contents =
                                                                content.contents.mapNotNull { ct ->
                                                                    ct?.copy(
                                                                        artists =
                                                                            ct.artists?.let { art ->
                                                                                if (art.size > 1) {
                                                                                    art.dropLast(1)
                                                                                } else {
                                                                                    art
                                                                                }
                                                                            },
                                                                    )
                                                                },
                                                        )
                                                    },
                                                navController = navController,
                                                viewModel = viewModel,
                                            )
                                        }
                                    } else {

                                        HomeItem(
                                            navController = navController,
                                            data = item,
                                        )
                                    }

                                }
                            }
                        }
                        item {
                            AnimatedVisibility(
                                homeListState == ListState.PAGINATING,
                                enter = expandVertically() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                CenterLoadingBox(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                )
                            }
                        }
                        if (homeListState == ListState.PAGINATION_EXHAUST) {
                            items(newRelease, key = { it.hashCode() }) {
                                AnimatedVisibility(
                                    visible = newRelease.isNotEmpty(),
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .padding(horizontal = 15.dp),
                                    ) {

                                        HomeItem(
                                            navController = navController,
                                            data = it,
                                        )
                                    }
                                }
                            }
                            item {
                                AnimatedVisibility(
                                    visible = moodMomentAndGenre != null,
                                ) {
                                    Box(
                                        modifier =
                                            Modifier
                                                .padding(horizontal = 15.dp),
                                    ) {
                                        moodMomentAndGenre?.let {
                                            MoodMomentAndGenre(
                                                mood = it,
                                                navController = navController,
                                            )
                                        }
                                    }
                                }
                            }
                            item {
                                Column(
                                    Modifier
                                        .padding(vertical = 10.dp)
                                        .padding(horizontal = 15.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    ChartTitle()
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Crossfade(targetState = regionChart) {
                                        Logger.w("HomeScreen", "regionChart: $it")
                                        if (it != null) {
                                            DropdownButton(
                                                items = CHART_SUPPORTED_COUNTRY.itemsData.toList(),
                                                defaultSelected =
                                                    CHART_SUPPORTED_COUNTRY.itemsData.getOrNull(
                                                        CHART_SUPPORTED_COUNTRY.items.indexOf(it),
                                                    )
                                                        ?: CHART_SUPPORTED_COUNTRY.itemsData[1],
                                            ) {
                                                viewModel.exploreChart(
                                                    CHART_SUPPORTED_COUNTRY.items[
                                                        CHART_SUPPORTED_COUNTRY.itemsData.indexOf(
                                                            it,
                                                        ),
                                                    ],
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Crossfade(
                                        targetState = chartLoading,
                                        label = "Chart",
                                    ) { loading ->
                                        if (!loading) {
                                            chart?.let {
                                                ChartData(
                                                    chart = it,
                                                    navController = navController,
                                                )
                                            }
                                        } else {
                                            CenterLoadingBox(
                                                modifier =
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .height(400.dp),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            EndOfPage()
                        }
                    }
                    }
                } else {
                    Column {
                        Spacer(
                            Modifier.height(
                                with(LocalDensity.current) {
                                    topAppBarHeightPx.toDp()
                                },
                            ),
                        )
                        HomeShimmer(isGrid = params == HOME_PARAMS_MIX_FOR_YOU)
                    }
                }
            }
        }
        AnimatedContent(
            targetState = scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset == 0,
            transitionSpec = {
                fadeIn(tween(300)).togetherWith(fadeOut(tween(300)))
            },
        ) { target ->
            Column(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .then(
                            if (target) {
                                Modifier.background(Color.Transparent)
                            } else {
                                Modifier
                                    .hazeEffect(hazeState, style = HazeMaterials.ultraThin()) {
                                        blurEnabled = true
                                    }
                            },
                        ).onGloballyPositioned { coordinates ->
                            topAppBarHeightPx = coordinates.size.height
                        },
            ) {
                AnimatedVisibility(
                    visible = isScrollingUp,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    HomeTopAppBar(navController, accountInfo)
                }
                AnimatedVisibility(
                    visible = !isScrollingUp,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    Spacer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(
                                    WindowInsets.statusBars,
                                ),
                    )
                }
                Row(
                    modifier =
                        Modifier
                            .horizontalScroll(chipRowState)
                            .padding(vertical = 8.dp, horizontal = 15.dp)
                            .background(Color.Transparent),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    listOfHomeChip.forEach { id ->
                        val isSelected =
                            when (params) {
                                HOME_PARAMS_MIX_FOR_YOU -> id == Res.string.mix_for_you
                                HOME_PARAMS_RELAX -> id == Res.string.relax
                                HOME_PARAMS_SLEEP -> id == Res.string.sleep
                                HOME_PARAMS_ENERGIZE -> id == Res.string.energize
                                HOME_PARAMS_SAD -> id == Res.string.sad
                                HOME_PARAMS_ROMANCE -> id == Res.string.romance
                                HOME_PARAMS_FEEL_GOOD -> id == Res.string.feel_good
                                HOME_PARAMS_WORKOUT -> id == Res.string.workout
                                HOME_PARAMS_PARTY -> id == Res.string.party
                                HOME_PARAMS_COMMUTE -> id == Res.string.commute
                                HOME_PARAMS_FOCUS -> id == Res.string.focus
                                else -> id == Res.string.all
                            }
                        Chip(
                            isAnimated = loading,
                            isAi = id == Res.string.mix_for_you,
                            isSelected = isSelected,
                            text = stringResource(id),
                        ) {
                            when (id) {
                                Res.string.all -> viewModel.setParams(null)
                                Res.string.mix_for_you -> viewModel.setParams(HOME_PARAMS_MIX_FOR_YOU)
                                Res.string.relax -> viewModel.setParams(HOME_PARAMS_RELAX)
                                Res.string.sleep -> viewModel.setParams(HOME_PARAMS_SLEEP)
                                Res.string.energize -> viewModel.setParams(HOME_PARAMS_ENERGIZE)
                                Res.string.sad -> viewModel.setParams(HOME_PARAMS_SAD)
                                Res.string.romance -> viewModel.setParams(HOME_PARAMS_ROMANCE)
                                Res.string.feel_good -> viewModel.setParams(HOME_PARAMS_FEEL_GOOD)
                                Res.string.workout -> viewModel.setParams(HOME_PARAMS_WORKOUT)
                                Res.string.party -> viewModel.setParams(HOME_PARAMS_PARTY)
                                Res.string.commute -> viewModel.setParams(HOME_PARAMS_COMMUTE)
                                Res.string.focus -> viewModel.setParams(HOME_PARAMS_FOCUS)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(navController: NavController, accountInfo: Pair<String?, String?>?) {
    TopAppBar(
        windowInsets =
            TopAppBarDefaults.windowInsets.exclude(
                TopAppBarDefaults.windowInsets.only(WindowInsetsSides.Start),
            ),
        navigationIcon = {
            androidx.compose.material3.IconButton(
                onClick = { navController.navigate(SettingsDestination()) }
            ) {
                if (!accountInfo?.second.isNullOrEmpty()) {
                    coil3.compose.AsyncImage(
                        model =
                            coil3.request.ImageRequest
                                .Builder(coil3.compose.LocalPlatformContext.current)
                                .data(accountInfo?.second)
                                .diskCachePolicy(coil3.request.CachePolicy.ENABLED)
                                .diskCacheKey(accountInfo?.second)
                                .crossfade(true)
                                .build(),
                        placeholder = echo.music.enhanced.ui.component.rememberHolderPainter(),
                        error = echo.music.enhanced.ui.component.rememberHolderPainter(),
                        contentDescription = null,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier =
                            Modifier
                                .size(30.dp)
                                .clip(
                                    androidx.compose.foundation.shape.CircleShape,
                                ),
                    )
                } else {
                    androidx.compose.material3.Icon(
                        imageVector = echoIcons.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.app_name),
                style = typo().titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        actions = {
            RippleIconButton(imageVector = echoIcons.History, tint = MaterialTheme.colorScheme.onBackground) {
                navController.navigate(RecentlySongsDestination)
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
    )
}

@Composable
fun AccountLayout(
    accountName: String,
    url: String,
) {
    Column {
        Text(
            text = stringResource(Res.string.welcome_back),
            style = typo().bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 3.dp),
        )
        if (accountName.isNotEmpty() && url.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp),
            ) {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalPlatformContext.current)
                            .data(url)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .diskCacheKey(url)
                            .crossfade(true)
                            .build(),
                    placeholder = rememberHolderPainter(),
                    error = rememberHolderPainter(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier
                            .size(40.dp)
                            .clip(
                                CircleShape,
                            ),
                )
                Text(
                    text = accountName,
                    style = typo().headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier =
                        Modifier
                            .padding(start = 8.dp),
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun QuickPicks(
    homeItem: HomeItem,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val lazyListState = rememberLazyGridState()
    val snapperFlingBehavior = rememberSnapFlingBehavior(SnapLayoutInfoProvider(lazyGridState = lazyListState, snapPosition = SnapPosition.Start))
    val density = LocalDensity.current
    var widthDp by remember {
        mutableStateOf(0.dp)
    }
    var bottomSheetShow by remember { mutableStateOf(false) }
    var track by remember { mutableStateOf<Track?>(null) }

    if (bottomSheetShow) {
        NowPlayingBottomSheet(
            onDismiss = { bottomSheetShow = false },
            song = track?.toSongEntity(),
            navController = navController,
        )
    }

    Column(
        Modifier
            .padding(vertical = 8.dp)
            .onGloballyPositioned { coordinates ->
                with(density) {
                    widthDp = (coordinates.size.width).toDp()
                }
            },
    ) {
        Text(
            text = stringResource(Res.string.let_s_start_with_a_radio),
            style = typo().bodySmall,
            modifier = Modifier.padding(bottom = 3.dp),
        )
        Text(
            text = stringResource(Res.string.quick_picks),
            style = typo().headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(4),
            modifier = Modifier.height(256.dp),
            state = lazyListState,
            flingBehavior = snapperFlingBehavior,
        ) {
            items(homeItem.contents, key = { it.hashCode() }) {
                if (it != null) {
                    QuickPicksItem(
                        onClick = {
                            val firstQueue: Track = it.toTrack()
                            viewModel.setQueueData(
                                QueueData.Data(
                                    listTracks = arrayListOf(firstQueue),
                                    firstPlayedTrack = firstQueue,
                                    playlistId = "RDAMVM${it.videoId}",
                                    playlistName = "\"${it.title}\" Radio",
                                    playlistType = PlaylistType.RADIO,
                                    continuation = null,
                                ),
                            )
                            viewModel.loadMediaItem(
                                firstQueue,
                                type = Config.SONG_CLICK,
                            )
                        },
                        onLongClick = {
                            track = it.toTrack()
                            bottomSheetShow = true
                        },
                        data = it,
                        widthDp = widthDp,
                    )
                }
            }
        }
    }
}

@Composable
fun MoodMomentAndGenre(
    mood: Mood,
    navController: NavController,
) {
    // Chip width used to be a hardcoded 160.dp that never divided evenly into the squircle card's
    // actual (post-padding, clipped) width, so the trailing chip in each row was cut off mid-chip
    // by the card's rounded corner instead of the row ending cleanly. Measuring the real available
    // width and sizing 2 chips per row to fill it exactly (matching the responsive pattern already
    // used by ChartData/QuickPicks below) removes the cut-off entirely.
    var containerWidthDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    Column(
        Modifier
            .padding(vertical = 8.dp)
            .onGloballyPositioned { coordinates ->
                with(density) { containerWidthDp = coordinates.size.width.toDp() }
            },
    ) {
        Text(
            text = stringResource(Res.string.let_s_pick_a_playlist_for_you),
            style = typo().bodyMedium,
            modifier = Modifier.padding(bottom = 3.dp),
        )
        val chipWidth = if (containerWidthDp > 0.dp) containerWidthDp / 2 else 160.dp
        // One block per section YouTube returned, headed by ITS OWN title. Hard-coding
        // "Moods & moment" / "Genre" here (and reading mood.moodsMoments / mood.genres by
        // index) mislabelled every row as soon as a signed-in account got an extra
        // "For you" section, and hid the real Genres section altogether.
        mood.sections.forEach { section ->
            val gridState = rememberLazyGridState()
            val flingBehavior = rememberSnapFlingBehavior(SnapLayoutInfoProvider(lazyGridState = gridState))
            Text(
                text = section.title,
                style = typo().headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
            )
            LazyHorizontalGrid(
                rows = GridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().height(210.dp),
                state = gridState,
                flingBehavior = flingBehavior,
            ) {
                items(section.items, key = { it.params }) { item ->

                    MoodMomentAndGenreHomeItem(
                        title = item.title,
                        stripeColor = item.stripeColor,
                        width = chipWidth,
                    ) {
                        navController.navigate(
                            MoodDestination(
                                item.params,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChartTitle() {
    Column {
        Text(
            text = stringResource(Res.string.what_is_best_choice_today),
            style = typo().bodyMedium,
            modifier = Modifier.padding(bottom = 3.dp),
        )
        Text(
            text = stringResource(Res.string.chart),
            style = typo().headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
        )
    }
}

@Composable
fun ChartData(
    chart: Chart,
    navController: NavController,
) {
    var gridWidthDp by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    val lazyListState2 = rememberLazyGridState()
    val snapperFlingBehavior2 = rememberSnapFlingBehavior(SnapLayoutInfoProvider(lazyGridState = lazyListState2))

    Column(
        Modifier.onGloballyPositioned { coordinates ->
            with(density) {
                gridWidthDp = (coordinates.size.width).toDp()
            }
        },
    ) {
        chart.listChartItem.forEach { item ->
            Text(
                text = item.title,
                style = typo().headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
            )
            val lazyListState = rememberLazyListState()
            val snapperFlingBehavior = rememberSnapFlingBehavior(SnapLayoutInfoProvider(lazyListState = lazyListState))
            // Was an unconstrained thumbSize = 160.dp default that didn't divide evenly into the
            // squircle card's real width, cutting the trailing card off mid-thumbnail. 2 cards per
            // row, sized to the same measured gridWidthDp the artist grid below already uses.
            val playlistThumbSize =
                if (gridWidthDp > 0.dp) ((gridWidthDp - 40.dp) / 2).coerceAtLeast(120.dp) else 160.dp
            LazyRow(modifier = Modifier.fillMaxWidth(), flingBehavior = snapperFlingBehavior) {
                items(item.playlists.size, key = { index ->
                    val data = item.playlists[index]
                    data.id + data.title + index
                }) {
                    HomeItemContentPlaylist(
                        thumbSize = playlistThumbSize,
                        onClick = {
                            navController.navigate(
                                PlaylistDestination(
                                    playlistId = item.playlists[it].id,
                                    isYourYouTubePlaylist = false,
                                ),
                            )
                        },
                        data = item.playlists[it],
                    )
                }
            }
        }
        Text(
            text = stringResource(Res.string.top_artists),
            style = typo().headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            modifier = Modifier.height(240.dp),
            state = lazyListState2,
            flingBehavior = snapperFlingBehavior2,
        ) {
            items(chart.artists.itemArtists.size, key = { index ->
                val item = chart.artists.itemArtists[index]
                item.title + item.browseId + index
            }) {
                val data = chart.artists.itemArtists[it]
                ItemArtistChart(
                    onClick = {
                        navController.navigate(
                            ArtistDestination(
                                channelId = data.browseId,
                            ),
                        )
                    },
                    data = data,
                    widthDp = gridWidthDp,
                )
            }
        }
    }
}