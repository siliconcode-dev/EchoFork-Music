package echo.music.enhanced.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import echo.music.enhanced.common.Config
import echo.music.enhanced.domain.data.entities.SongEntity
import echo.music.enhanced.domain.data.entities.analytics.PlaybackEventEntity
import echo.music.enhanced.domain.data.model.browse.album.Track
import echo.music.enhanced.domain.data.model.home.HomeItem
import echo.music.enhanced.domain.mediaservice.handler.PlaylistType
import echo.music.enhanced.domain.mediaservice.handler.QueueData
import echo.music.enhanced.domain.utils.toSongEntity
import echo.music.enhanced.domain.utils.toTrack
import echo.music.enhanced.extension.artworkScrimBrush
import echo.music.enhanced.ui.component.NowPlayingBottomSheet
import echo.music.enhanced.ui.component.rememberHolderPainter
import echo.music.enhanced.ui.theme.typo
import echo.music.enhanced.viewModel.HomeViewModel
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.keep_listening
import echomusic.composeapp.generated.resources.let_s_start_with_a_radio
import echomusic.composeapp.generated.resources.quick_picks
import echomusic.composeapp.generated.resources.speed_dial
import org.jetbrains.compose.resources.stringResource

/**
 * Better Echo only: hero-carousel restyle of Quick Picks, porting upstream's real
 * `HorizontalCenteredHeroCarousel` treatment (see v0.1.12 plan grounding). Reuses the exact
 * click/long-click handling the Classic [QuickPicks] grid already uses ([QueueData.Data] radio
 * queue + [NowPlayingBottomSheet]) rather than rebuilding it.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuickPicksHero(
    homeItem: HomeItem,
    navController: NavController,
    viewModel: HomeViewModel,
) {
    var bottomSheetShow by remember { mutableStateOf(false) }
    var selectedTrack by remember { mutableStateOf<Track?>(null) }
    if (bottomSheetShow) {
        NowPlayingBottomSheet(
            onDismiss = { bottomSheetShow = false },
            song = selectedTrack?.toSongEntity(),
            navController = navController,
        )
    }
    val items = homeItem.contents.filterNotNull()
    if (items.isEmpty()) return
    Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
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
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        )
        HorizontalCenteredHeroCarousel(
            state = rememberCarouselState { items.size },
            maxItemWidth = 250.dp,
            itemSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth().height(290.dp),
        ) { index ->
            val content = items[index]
            val itemShape = MaterialTheme.shapes.extraLarge
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .maskClip(itemShape)
                        .maskBorder(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), itemShape)
                        .combinedClickable(
                            onClick = {
                                val firstQueue = content.toTrack()
                                viewModel.setQueueData(
                                    QueueData.Data(
                                        listTracks = arrayListOf(firstQueue),
                                        firstPlayedTrack = firstQueue,
                                        playlistId = "RDAMVM${content.videoId}",
                                        playlistName = "\"${content.title}\" Radio",
                                        playlistType = PlaylistType.RADIO,
                                        continuation = null,
                                    ),
                                )
                                viewModel.loadMediaItem(firstQueue, type = Config.SONG_CLICK)
                            },
                            onLongClick = {
                                selectedTrack = content.toTrack()
                                bottomSheetShow = true
                            },
                        ),
            ) {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalPlatformContext.current)
                            .data(content.thumbnails.lastOrNull()?.url)
                            .crossfade(true)
                            .build(),
                    contentDescription = content.title,
                    contentScale = ContentScale.Crop,
                    placeholder = rememberHolderPainter(),
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .align(Alignment.BottomCenter)
                            .background(artworkScrimBrush(Color.Black.copy(alpha = 0.75f))),
                )
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(12.dp)) {
                    Text(
                        text = content.title,
                        color = Color.White,
                        style = typo().titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    content.artists?.joinToString(", ") { it.name }?.let {
                        Text(
                            text = it,
                            color = Color.White.copy(alpha = 0.7f),
                            style = typo().bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Better Echo only: a full-bleed square art tile shared by [SpeedDialSection] and
 * [KeepListeningSection] (both are plain most-played/recently-played song lists).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongTile(
    song: SongEntity,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(MaterialTheme.shapes.medium)
                .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(song.thumbnails)
                    .crossfade(true)
                    .build(),
            contentDescription = song.title,
            contentScale = ContentScale.Crop,
            placeholder = rememberHolderPainter(),
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .align(Alignment.BottomCenter)
                    .background(artworkScrimBrush(Color.Black.copy(alpha = 0.8f))),
        )
        Text(
            text = song.title,
            color = Color.White,
            style = typo().labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.BottomStart).padding(6.dp),
        )
    }
}

/**
 * Better Echo only: Home's Speed Dial section — a faithful port of upstream's paged square grid
 * (dynamic column/row count from available width, page-dot indicators), minus the pin feature and
 * reserved shuffle tile, since this fork has no pinning backend (see v0.1.12 plan decision #1).
 * Backed by [HomeViewModel.speedDialItems] (most-played songs, no radio — direct play like
 * upstream's own "songs play directly" behavior).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeedDialSection(
    items: List<SongEntity>,
    navController: NavController,
    viewModel: HomeViewModel,
) {
    if (items.isEmpty()) return
    var bottomSheetShow by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<SongEntity?>(null) }
    if (bottomSheetShow) {
        NowPlayingBottomSheet(onDismiss = { bottomSheetShow = false }, song = selectedSong, navController = navController)
    }
    val speedDialTitle = stringResource(Res.string.speed_dial)
    Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Text(
            text = speedDialTitle,
            style = typo().headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val targetItemSize = 100.dp
            val columns = (maxWidth / targetItemSize).toInt().coerceAtLeast(3)
            val maxRowsForColumns = if (columns >= 6) 1 else if (columns >= 4) 2 else 3
            // Cap rows to what the actual item count needs — reserving a full 3x3 grid when
            // there are only 1-2 most-played songs left the card mostly blank space below them.
            val rows = maxRowsForColumns.coerceAtMost((items.size + columns - 1) / columns).coerceAtLeast(1)
            val itemsPerPage = columns * rows
            val itemWidth = maxWidth / columns
            val pageCount = (items.size + itemsPerPage - 1) / itemsPerPage
            val pagerState = rememberPagerState(pageCount = { pageCount })
            Column {
                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 12.dp,
                    modifier = Modifier.fillMaxWidth().height(itemWidth * rows),
                ) { page ->
                    Column {
                        for (row in 0 until rows) {
                            Row(Modifier.fillMaxWidth()) {
                                for (col in 0 until columns) {
                                    val itemIndex = page * itemsPerPage + row * columns + col
                                    val song = items.getOrNull(itemIndex)
                                    Box(Modifier.width(itemWidth).height(itemWidth).padding(4.dp)) {
                                        if (song != null) {
                                            SongTile(
                                                song = song,
                                                onClick = {
                                                    val track = song.toTrack()
                                                    viewModel.setQueueData(
                                                        QueueData.Data(
                                                            listTracks = arrayListOf(track),
                                                            firstPlayedTrack = track,
                                                            playlistId = null,
                                                            playlistName = speedDialTitle,
                                                            playlistType = PlaylistType.PLAYLIST,
                                                            continuation = null,
                                                        ),
                                                    )
                                                    viewModel.loadMediaItem(track, type = Config.SONG_CLICK)
                                                },
                                                onLongClick = {
                                                    selectedSong = song
                                                    bottomSheetShow = true
                                                },
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (pageCount > 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        repeat(pageCount) { page ->
                            val selected = pagerState.currentPage == page
                            Box(
                                modifier =
                                    Modifier
                                        .padding(horizontal = 3.dp)
                                        .size(if (selected) 8.dp else 6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (selected) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.outlineVariant
                                            },
                                        ),
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Better Echo only: Home's Keep Listening section, backed by [HomeViewModel.keepListening] — the
 * same [echo.music.enhanced.domain.repository.AnalyticsRepository.getPlaybackEventsByOffset] join
 * `AnalyticsViewModel.getRecentlyRecord()` already uses (see v0.1.12 plan decision #2), so ordering
 * reflects real play timestamps rather than `inLibrary DESC`.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeepListeningSection(
    records: List<Pair<PlaybackEventEntity, SongEntity>>,
    navController: NavController,
    viewModel: HomeViewModel,
) {
    val distinctSongs = records.map { it.second }.distinctBy { it.videoId }
    if (distinctSongs.isEmpty()) return
    var bottomSheetShow by remember { mutableStateOf(false) }
    var selectedSong by remember { mutableStateOf<SongEntity?>(null) }
    if (bottomSheetShow) {
        NowPlayingBottomSheet(onDismiss = { bottomSheetShow = false }, song = selectedSong, navController = navController)
    }
    val keepListeningTitle = stringResource(Res.string.keep_listening)
    val rows = if (distinctSongs.size > 6) 2 else 1
    Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Text(
            text = keepListeningTitle,
            style = typo().headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(rows),
            modifier = Modifier.fillMaxWidth().height(110.dp * rows),
        ) {
            items(distinctSongs, key = { it.videoId }) { song ->
                SongTile(
                    song = song,
                    onClick = {
                        val track = song.toTrack()
                        viewModel.setQueueData(
                            QueueData.Data(
                                listTracks = arrayListOf(track),
                                firstPlayedTrack = track,
                                playlistId = null,
                                playlistName = keepListeningTitle,
                                playlistType = PlaylistType.PLAYLIST,
                                continuation = null,
                            ),
                        )
                        viewModel.loadMediaItem(track, type = Config.SONG_CLICK)
                    },
                    onLongClick = {
                        selectedSong = song
                        bottomSheetShow = true
                    },
                    modifier = Modifier.width(110.dp).padding(4.dp),
                )
            }
        }
    }
}
