package echo.music.enhanced.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import echo.music.enhanced.domain.data.entities.SongEntity
import echo.music.enhanced.domain.utils.connectArtists
import echo.music.enhanced.expect.ui.toImageBitmap
import echo.music.enhanced.logger.Logger
import echo.music.enhanced.ui.component.ExplicitBadge
import echo.music.enhanced.ui.component.HeartCheckBox
import echo.music.enhanced.ui.component.PlayPauseButton
import echo.music.enhanced.ui.component.ScallopedShape
import echo.music.enhanced.ui.component.rememberHolderPainter
import echo.music.enhanced.ui.icon.SkipNext
import echo.music.enhanced.ui.icon.SkipPrevious
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.theme.typo
import echo.music.enhanced.viewModel.SharedViewModel
import echo.music.enhanced.viewModel.UIEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Better Echo only, faithful port of upstream Echo Music's real `NewMiniPlayer` (default there,
 * `useNewMiniPlayerDesign = true`): a floating pill with a circular ring-progress thumbnail and a
 * spinning cookie-shaped play/pause button. Shares [songEntity]/[liked]/[isPlaying]/[loading]/
 * [animatedProgress]/[offsetX]/[offsetY]/[coroutineScope] with [MiniPlayer]'s single shared-state
 * setup — no gesture/state plumbing is duplicated here, only the layout/drawing.
 */
@Composable
fun NewMiniPlayer(
    modifier: Modifier,
    songEntity: SongEntity?,
    liked: Boolean,
    isPlaying: Boolean,
    loading: Boolean,
    animatedProgress: Float,
    offsetX: Animatable<Float, AnimationVector1D>,
    offsetY: Animatable<Float, AnimationVector1D>,
    paletteColor: Color,
    coroutineScope: CoroutineScope,
    sharedViewModel: SharedViewModel,
    onClick: () -> Unit,
    onClose: () -> Unit,
    onBitmapCaptured: (ImageBitmap) -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = paletteColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier =
            modifier
                .widthIn(max = 340.dp)
                .clipToBounds()
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .clickable(onClick = onClick)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = {},
                        onVerticalDrag = { change: PointerInputChange, dragAmount: Float ->
                            if (offsetY.value + dragAmount > 0) {
                                coroutineScope.launch {
                                    change.consume()
                                    offsetY.animateTo(offsetY.value + 2 * dragAmount)
                                }
                            }
                        },
                        onDragCancel = { coroutineScope.launch { offsetY.animateTo(0f) } },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetY.value > 70) onClose()
                                offsetY.animateTo(0f)
                            }
                        },
                    )
                },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragStart = {},
                                onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                                    coroutineScope.launch {
                                        change.consume()
                                        offsetX.animateTo(offsetX.value + dragAmount * 2)
                                    }
                                },
                                onDragCancel = {
                                    coroutineScope.launch {
                                        if (offsetX.value > 200) {
                                            sharedViewModel.onUIEvent(UIEvent.Previous)
                                        } else if (offsetX.value < -120) {
                                            sharedViewModel.onUIEvent(UIEvent.Next)
                                        }
                                        offsetX.animateTo(0f)
                                    }
                                },
                                onDragEnd = {
                                    coroutineScope.launch {
                                        if (offsetX.value > 200) {
                                            sharedViewModel.onUIEvent(UIEvent.Previous)
                                        } else if (offsetX.value < -120) {
                                            sharedViewModel.onUIEvent(UIEvent.Next)
                                        }
                                        offsetX.animateTo(0f)
                                    }
                                },
                            )
                        },
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidthPx = 3.dp.toPx()
                    // Force a perfect circle regardless of the Canvas's actual (width, height):
                    // drawArc's implicit bounds default to the full draw-scope rect, which draws
                    // an ellipse whenever that rect isn't exactly square.
                    val diameter = minOf(size.width, size.height) - strokeWidthPx
                    val arcSize = Size(diameter, diameter)
                    val arcTopLeft =
                        Offset(
                            (size.width - diameter) / 2f,
                            (size.height - diameter) / 2f,
                        )
                    drawArc(
                        color = textColor.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidthPx, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                    )
                    drawArc(
                        color = textColor,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidthPx, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                    )
                }
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalPlatformContext.current)
                            .data(songEntity?.thumbnails)
                            .crossfade(550)
                            .build(),
                    placeholder = rememberHolderPainter(),
                    error = rememberHolderPainter(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onSuccess = { onBitmapCaptured(it.result.image.toImageBitmap()) },
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f).wrapContentHeight()) {
                Text(
                    text = songEntity?.title ?: "",
                    style = typo().labelSmall,
                    color = textColor,
                    maxLines = 1,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .basicMarquee(iterations = Int.MAX_VALUE, animationMode = MarqueeAnimationMode.Immediately)
                            .focusable(),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.animation.AnimatedVisibility(visible = songEntity?.isExplicit == true) {
                        ExplicitBadge(modifier = Modifier.size(18.dp).padding(end = 4.dp))
                    }
                    Text(
                        text = songEntity?.artistName?.connectArtists() ?: "",
                        style = typo().bodySmall,
                        color = textColor.copy(alpha = 0.7f),
                        maxLines = 1,
                        modifier =
                            Modifier
                                .weight(1f)
                                .basicMarquee(iterations = Int.MAX_VALUE, animationMode = MarqueeAnimationMode.Immediately)
                                .focusable(),
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { sharedViewModel.onUIEvent(UIEvent.Previous) }, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = echoIcons.SkipPrevious, contentDescription = null, tint = textColor)
            }
            CookiePlayPauseButton(isPlaying = isPlaying, loading = loading, tint = textColor) {
                sharedViewModel.onUIEvent(UIEvent.PlayPause)
            }
            IconButton(onClick = { sharedViewModel.onUIEvent(UIEvent.Next) }, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = echoIcons.SkipNext, contentDescription = null, tint = textColor)
            }
        }
    }
}

@Composable
private fun CookiePlayPauseButton(
    isPlaying: Boolean,
    loading: Boolean,
    tint: Color,
    onClick: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cookieRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isPlaying) 360f else 0f,
        animationSpec =
            androidx.compose.animation.core.infiniteRepeatable(
                animation = tween(durationMillis = 8000, easing = LinearEasing),
            ),
        label = "cookieRotation",
    )
    Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = if (isPlaying) rotation else 0f }
                    .clip(ScallopedShape(bumps = 8))
                    .background(tint.copy(alpha = 0.15f)),
        )
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = tint, strokeWidth = 2.dp)
        } else {
            PlayPauseButton(isPlaying = isPlaying, modifier = Modifier.size(32.dp), tint = tint, onClick = onClick)
        }
    }
}

/**
 * Better Echo only, faithful port of upstream's real `LegacyMiniPlayer`: a full-width bottom bar
 * with a thin bottom progress track, play-pause + skip-next only (no prev button, matching
 * upstream). Shares state with [MiniPlayer] the same way [NewMiniPlayer] does.
 */
@Composable
fun LegacyMiniPlayer(
    modifier: Modifier,
    songEntity: SongEntity?,
    liked: Boolean,
    isPlaying: Boolean,
    loading: Boolean,
    animatedProgress: Float,
    offsetX: Animatable<Float, AnimationVector1D>,
    offsetY: Animatable<Float, AnimationVector1D>,
    coroutineScope: CoroutineScope,
    sharedViewModel: SharedViewModel,
    onClick: () -> Unit,
    onClose: () -> Unit,
    onBitmapCaptured: (ImageBitmap) -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    Card(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier =
            modifier
                .fillMaxWidth()
                .clipToBounds()
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .clickable(onClick = onClick)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragStart = {},
                        onVerticalDrag = { change: PointerInputChange, dragAmount: Float ->
                            if (offsetY.value + dragAmount > 0) {
                                coroutineScope.launch {
                                    change.consume()
                                    offsetY.animateTo(offsetY.value + 2 * dragAmount)
                                }
                            }
                        },
                        onDragCancel = { coroutineScope.launch { offsetY.animateTo(0f) } },
                        onDragEnd = {
                            coroutineScope.launch {
                                if (offsetY.value > 70) onClose()
                                offsetY.animateTo(0f)
                            }
                        },
                    )
                },
    ) {
        Box(modifier = Modifier.fillMaxHeight()) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
                Row(
                    modifier =
                        Modifier
                            .weight(1f)
                            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures(
                                    onDragStart = {},
                                    onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                                        coroutineScope.launch {
                                            change.consume()
                                            offsetX.animateTo(offsetX.value + dragAmount * 2)
                                        }
                                    },
                                    onDragCancel = {
                                        coroutineScope.launch {
                                            if (offsetX.value > 200) {
                                                sharedViewModel.onUIEvent(UIEvent.Previous)
                                            } else if (offsetX.value < -120) {
                                                sharedViewModel.onUIEvent(UIEvent.Next)
                                            }
                                            offsetX.animateTo(0f)
                                        }
                                    },
                                    onDragEnd = {
                                        coroutineScope.launch {
                                            if (offsetX.value > 200) {
                                                sharedViewModel.onUIEvent(UIEvent.Previous)
                                            } else if (offsetX.value < -120) {
                                                sharedViewModel.onUIEvent(UIEvent.Next)
                                            }
                                            offsetX.animateTo(0f)
                                        }
                                    },
                                )
                            },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AsyncImage(
                        model =
                            ImageRequest
                                .Builder(LocalPlatformContext.current)
                                .data(songEntity?.thumbnails)
                                .crossfade(550)
                                .build(),
                        placeholder = rememberHolderPainter(),
                        error = rememberHolderPainter(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        onSuccess = { onBitmapCaptured(it.result.image.toImageBitmap()) },
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(6.dp)),
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f).wrapContentHeight()) {
                        Text(
                            text = songEntity?.title ?: "",
                            style = typo().labelSmall,
                            color = textColor,
                            maxLines = 1,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .basicMarquee(iterations = Int.MAX_VALUE, animationMode = MarqueeAnimationMode.Immediately)
                                    .focusable(),
                        )
                        Text(
                            text = songEntity?.artistName?.connectArtists() ?: "",
                            style = typo().bodySmall,
                            color = textColor.copy(alpha = 0.7f),
                            maxLines = 1,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .basicMarquee(iterations = Int.MAX_VALUE, animationMode = MarqueeAnimationMode.Immediately)
                                    .focusable(),
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                HeartCheckBox(checked = liked, size = 26, tint = textColor) {
                    sharedViewModel.onUIEvent(UIEvent.ToggleLike)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Crossfade(targetState = loading, label = "") {
                    if (it) {
                        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = textColor, strokeWidth = 2.dp)
                        }
                    } else {
                        PlayPauseButton(isPlaying = isPlaying, modifier = Modifier.size(40.dp), tint = textColor) {
                            sharedViewModel.onUIEvent(UIEvent.PlayPause)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { sharedViewModel.onUIEvent(UIEvent.Next) }, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = echoIcons.SkipNext, contentDescription = null, tint = textColor)
                }
            }
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.BottomCenter),
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(color = textColor.copy(alpha = 0.15f))
                    drawRect(color = textColor, size = size.copy(width = size.width * animatedProgress))
                }
            }
        }
    }
}
