package echo.music.enhanced.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.domain.mediaservice.handler.ControlState
import echo.music.enhanced.domain.mediaservice.handler.RepeatState
import echo.music.enhanced.ui.icon.Pause
import echo.music.enhanced.ui.icon.PauseCircle
import echo.music.enhanced.ui.icon.PlayArrow
import echo.music.enhanced.ui.icon.PlayCircle
import echo.music.enhanced.ui.icon.Repeat
import echo.music.enhanced.ui.icon.RepeatOne
import echo.music.enhanced.ui.icon.Shuffle
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.icon.SkipNext
import echo.music.enhanced.ui.icon.SkipPrevious
import echo.music.enhanced.ui.theme.seed
import echo.music.enhanced.viewModel.UIEvent

@Composable
fun PlayerControlLayout(
    controllerState: ControlState,
    isSmallSize: Boolean = false,
    // Bare ▶ / ⏸ glyphs instead of the disc-enclosed PlayCircle/PauseCircle pair.
    // The desktop capsule asks for these; Now Playing keeps the discs.
    plainPlayPause: Boolean = false,
    // The capsule already pads its own edges; stacking this 20dp on top of that
    // read as a hole at both ends of the transport cluster.
    horizontalPadding: Dp = 20.dp,
    // Tint for the ACTIVE shuffle/repeat state. The default keeps the raw seed (#8ECAE6) every
    // existing call site had; the capsule passes a theme-aware colour because pastel seed on a
    // light glass surface is nearly invisible.
    activeColor: Color = seed,
    contentColor: Color = Color.White,
    interfaceMode: String = DataStoreManager.INTERFACE_CLASSIC,
    onUIEvent: (UIEvent) -> Unit,
) {
    // Better Echo gets its own 3-button hero row + a separate shuffle/repeat chip row (v0.1.15) —
    // only for the full-size Now Playing cluster; a compact caller (e.g. a desktop capsule) stays
    // on the classic single-row layout regardless of interface mode, since a two-row layout would
    // not fit its available height.
    if (interfaceMode == DataStoreManager.INTERFACE_BETTER_ECHO && !isSmallSize) {
        BetterEchoPlayerControls(
            controllerState = controllerState,
            horizontalPadding = horizontalPadding,
            activeColor = activeColor,
            contentColor = contentColor,
            onUIEvent = onUIEvent,
        )
        return
    }
    val height = if (isSmallSize) 48.dp else 96.dp
    val smallIcon = if (isSmallSize) 20.dp to 28.dp else 32.dp to 42.dp
    val mediumIcon = if (isSmallSize) 28.dp to 38.dp else 42.dp to 52.dp
    val bigIcon = if (isSmallSize) 38.dp to 48.dp else 72.dp to 96.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = horizontalPadding),
    ) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .background(Color.Transparent)
                        .size(smallIcon.second)
                        .aspectRatio(1f)
                        .clip(
                            CircleShape,
                        )
                        .clickable {
                            onUIEvent(UIEvent.Shuffle)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.isShuffle, label = "Shuffle Button") { isShuffle ->
                    if (!isShuffle) {
                        Icon(
                            imageVector = echoIcons.Shuffle,
                            tint = contentColor,
                            contentDescription = "",
                            modifier = Modifier.size(smallIcon.first),
                        )
                    } else {
                        Icon(
                            imageVector = echoIcons.Shuffle,
                            tint = activeColor,
                            contentDescription = "",
                            modifier = Modifier.size(smallIcon.first),
                        )
                    }
                }
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .background(Color.Transparent)
                        .size(mediumIcon.second)
                        .aspectRatio(1f)
                        .clip(
                            CircleShape,
                        )
                        .clickable {
                            if (controllerState.isPreviousAvailable) {
                                onUIEvent(UIEvent.Previous)
                            }
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = echoIcons.SkipPrevious,
                    tint = if (controllerState.isPreviousAvailable) contentColor else contentColor.copy(alpha = 0.4f),
                    contentDescription = "",
                    modifier = Modifier.size(mediumIcon.first),
                )
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .background(Color.Transparent)
                        .size(bigIcon.second)
                        .aspectRatio(1f)
                        .clip(
                            CircleShape,
                        )
                        .clickable {
                            onUIEvent(UIEvent.PlayPause)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.isPlaying) { isPlaying ->
                    if (!isPlaying) {
                        Icon(
                            imageVector = if (plainPlayPause) echoIcons.PlayArrow else echoIcons.PlayCircle,
                            tint = contentColor,
                            contentDescription = "",
                            modifier = Modifier.size(bigIcon.first),
                        )
                    } else {
                        Icon(
                            imageVector = if (plainPlayPause) echoIcons.Pause else echoIcons.PauseCircle,
                            tint = contentColor,
                            contentDescription = "",
                            modifier = Modifier.size(bigIcon.first),
                        )
                    }
                }
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .background(Color.Transparent)
                        .size(mediumIcon.second)
                        .aspectRatio(1f)
                        .clip(
                            CircleShape,
                        )
                        .clickable {
                            if (controllerState.isNextAvailable) {
                                onUIEvent(UIEvent.Next)
                            }
                        },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = echoIcons.SkipNext,
                    tint = if (controllerState.isNextAvailable) contentColor else contentColor.copy(alpha = 0.4f),
                    contentDescription = "",
                    modifier = Modifier.size(mediumIcon.first),
                )
            }
        }
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Box(
                modifier =
                    Modifier
                        .size(smallIcon.second)
                        .aspectRatio(1f)
                        .clip(
                            CircleShape,
                        )
                        .clickable {
                            onUIEvent(UIEvent.Repeat)
                        },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.repeatState) { rs ->
                    when (rs) {
                        is RepeatState.None -> {
                            Icon(
                                imageVector = echoIcons.Repeat,
                                tint = contentColor,
                                contentDescription = "",
                                modifier = Modifier.size(smallIcon.first),
                            )
                        }

                        RepeatState.All -> {
                            Icon(
                                imageVector = echoIcons.Repeat,
                                tint = activeColor,
                                contentDescription = "",
                                modifier = Modifier.size(smallIcon.first),
                            )
                        }

                        RepeatState.One -> {
                            Icon(
                                imageVector = echoIcons.RepeatOne,
                                tint = activeColor,
                                contentDescription = "",
                                modifier = Modifier.size(smallIcon.first),
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Better Echo's transport-control cluster (v0.1.15): a 3-button hero row (prev/play/next, faithful
 * to upstream's translucent-circle prev/next + solid rotating "cookie" play/pause) plus a small
 * shuffle/repeat chip row underneath — upstream moves shuffle/repeat into its docked queue toolbar,
 * which doesn't exist here yet (that's v0.1.18), so this chip row is their interim home.
 */
@Composable
private fun BetterEchoPlayerControls(
    controllerState: ControlState,
    horizontalPadding: Dp,
    activeColor: Color,
    contentColor: Color,
    onUIEvent: (UIEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .padding(horizontal = horizontalPadding),
        ) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                BetterEchoTransportButton(
                    icon = echoIcons.SkipPrevious,
                    enabled = controllerState.isPreviousAvailable,
                    contentColor = contentColor,
                    size = 52.dp,
                    iconSize = 30.dp,
                    onClick = { if (controllerState.isPreviousAvailable) onUIEvent(UIEvent.Previous) },
                )
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                BetterEchoCookiePlayPauseButton(
                    isPlaying = controllerState.isPlaying,
                    activeColor = activeColor,
                    contentColor = contentColor,
                    onClick = { onUIEvent(UIEvent.PlayPause) },
                )
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                BetterEchoTransportButton(
                    icon = echoIcons.SkipNext,
                    enabled = controllerState.isNextAvailable,
                    contentColor = contentColor,
                    size = 52.dp,
                    iconSize = 30.dp,
                    onClick = { if (controllerState.isNextAvailable) onUIEvent(UIEvent.Next) },
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable { onUIEvent(UIEvent.Shuffle) },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.isShuffle, label = "Shuffle Chip") { isShuffle ->
                    Icon(
                        imageVector = echoIcons.Shuffle,
                        tint = if (isShuffle) activeColor else contentColor.copy(alpha = 0.7f),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Spacer(Modifier.width(28.dp))
            Box(
                modifier =
                    Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .clickable { onUIEvent(UIEvent.Repeat) },
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = controllerState.repeatState, label = "Repeat Chip") { rs ->
                    when (rs) {
                        is RepeatState.None ->
                            Icon(
                                imageVector = echoIcons.Repeat,
                                tint = contentColor.copy(alpha = 0.7f),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp),
                            )

                        RepeatState.All ->
                            Icon(
                                imageVector = echoIcons.Repeat,
                                tint = activeColor,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp),
                            )

                        RepeatState.One ->
                            Icon(
                                imageVector = echoIcons.RepeatOne,
                                tint = activeColor,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp),
                            )
                    }
                }
            }
        }
    }
}

/** Prev/next: a translucent circle backdrop + a spring press-scale, matching upstream's `FilledIconButton` treatment. */
@Composable
private fun BetterEchoTransportButton(
    icon: ImageVector,
    enabled: Boolean,
    contentColor: Color,
    size: Dp,
    iconSize: Dp,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by
        animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
            label = "transportPressScale",
        )
    Box(
        modifier =
            Modifier
                .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
                .size(size)
                .clip(CircleShape)
                .background(contentColor.copy(alpha = 0.12f))
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            tint = if (enabled) contentColor else contentColor.copy(alpha = 0.4f),
            contentDescription = "",
            modifier = Modifier.size(iconSize),
        )
    }
}

/**
 * Play/pause: a solid 9-bump [ScallopedShape] "cookie" that spins continuously while playing
 * (same 8s-linear idiom as [echo.music.enhanced.ui.screen.BetterEchoMiniPlayer]'s smaller version)
 * and morphs its own scallop depth down to a plain circle when paused, plus the same press-scale
 * as the prev/next buttons.
 */
@Composable
private fun BetterEchoCookiePlayPauseButton(
    isPlaying: Boolean,
    activeColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "nowPlayingCookieRotation")
    val rotation by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = if (isPlaying) 360f else 0f,
            animationSpec = infiniteRepeatable(animation = tween(durationMillis = 8000, easing = LinearEasing)),
            label = "nowPlayingCookieRotation",
        )
    val amplitude by
        animateFloatAsState(
            targetValue = if (isPlaying) 0.08f else 0f,
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 300f),
            label = "cookieAmplitude",
        )
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by
        animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
            label = "cookiePressScale",
        )
    Box(
        modifier =
            Modifier
                .size(96.dp)
                .graphicsLayer { scaleX = pressScale; scaleY = pressScale }
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = if (isPlaying) rotation else 0f }
                    .clip(ScallopedShape(bumps = 9, amplitude = amplitude))
                    .background(activeColor),
        )
        Crossfade(targetState = isPlaying, label = "Better Echo Play Pause Icon") { playing ->
            Icon(
                imageVector = if (!playing) echoIcons.PlayArrow else echoIcons.Pause,
                tint = contentColor,
                contentDescription = "",
                modifier = Modifier.size(40.dp),
            )
        }
    }
}