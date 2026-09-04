package echo.music.enhanced.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.extension.GradientAngle
import echo.music.enhanced.extension.GradientOffset
import echo.music.enhanced.extension.smoothScrimBrush
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin

/**
 * Better Echo's Now Playing / Queue backdrop system — a switchable alternative to the plain
 * diagonal-gradient backdrop, mirroring upstream Echo Music's real `PlayerBackgroundStyle` set
 * (minus `APPLE_MUSIC`'s live canvas-video compositing and `LIVE_MESH`, both deferred).
 *
 * `GRADIENT` is intentionally NOT one of the branches here — `NowPlayingScreen.kt` keeps its
 * existing diagonal-gradient code inline, byte-for-byte, so that style stays pixel-identical to
 * today for anyone who doesn't touch the new picker. Only [QueueBottomSheet] (which has no prior
 * gradient look to preserve) routes its `GRADIENT` selection through [GradientBackgroundLayer]
 * below.
 */
@Composable
fun NowPlayingBackground(
    style: String,
    glowSwatches: List<Color>,
    thumbnailBitmap: ImageBitmap?,
    backdropColor: Color,
    isPlaying: Boolean,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier,
    startColor: Color = backdropColor,
    endColor: Color = backdropColor,
) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(450))
    }
    Box(modifier = modifier.graphicsLayer { this.alpha = alpha.value }) {
        when (style) {
            DataStoreManager.Values.BETTER_ECHO_NOW_PLAYING_BG_BLUR ->
                BlurBackgroundLayer(thumbnailBitmap, isLightTheme, Modifier.fillMaxSize())

            DataStoreManager.Values.BETTER_ECHO_NOW_PLAYING_BG_GLOW_ANIMATED ->
                GlowAnimatedBackgroundLayer(
                    glowSwatches = glowSwatches,
                    backdropColor = backdropColor,
                    isPlaying = isPlaying,
                    isLightTheme = isLightTheme,
                    modifier = Modifier.fillMaxSize(),
                )

            DataStoreManager.Values.BETTER_ECHO_NOW_PLAYING_BG_ARTWORK_BLEND ->
                ArtworkBlendBackgroundLayer(thumbnailBitmap, isLightTheme, Modifier.fillMaxSize())

            else ->
                GradientBackgroundLayer(startColor, endColor, backdropColor, Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun GradientBackgroundLayer(
    startColor: Color,
    endColor: Color,
    backdropColor: Color,
    modifier: Modifier,
) {
    val gradientOffset = remember { GradientOffset(GradientAngle.CW135) }
    Box(
        modifier =
            modifier
                .background(backdropColor)
                .drawBehind {
                    drawRect(
                        brush =
                            Brush.linearGradient(
                                colors = listOf(startColor, endColor),
                                start = gradientOffset.start,
                                end = gradientOffset.end,
                            ),
                    )
                    drawRect(
                        brush =
                            smoothScrimBrush(
                                from = backdropColor.copy(alpha = 0f),
                                to = backdropColor,
                                startY = 0f,
                                endY = size.height * 0.95f,
                            ),
                    )
                },
    )
}

@Composable
private fun BlurBackgroundLayer(
    thumbnailBitmap: ImageBitmap?,
    isLightTheme: Boolean,
    modifier: Modifier,
) {
    val hazeState = rememberHazeState(blurEnabled = true)
    Box(modifier = modifier) {
        if (thumbnailBitmap != null) {
            Image(
                bitmap = thumbnailBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().hazeSource(hazeState),
            )
        } else {
            Box(Modifier.fillMaxSize().background(Color.Black))
        }
        Box(
            modifier =
                Modifier.fillMaxSize().hazeEffect(hazeState) {
                    style =
                        HazeStyle(
                            backgroundColor = Color.Transparent,
                            tints =
                                listOf(
                                    HazeTint(
                                        if (isLightTheme) {
                                            Color.White.copy(alpha = 0.35f)
                                        } else {
                                            Color.Black.copy(alpha = 0.35f)
                                        },
                                    ),
                                ),
                            blurRadius = 120.dp,
                            noiseFactor = 0f,
                        )
                },
        )
    }
}

@Composable
private fun ArtworkBlendBackgroundLayer(
    thumbnailBitmap: ImageBitmap?,
    isLightTheme: Boolean,
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        BlurBackgroundLayer(thumbnailBitmap, isLightTheme, Modifier.fillMaxSize())
        if (thumbnailBitmap != null) {
            val maskColor = if (isLightTheme) Color.White else Color.Black
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.65f)
                        .align(Alignment.TopCenter)
                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush =
                                    Brush.verticalGradient(
                                        colorStops =
                                            arrayOf(
                                                0.00f to maskColor,
                                                0.75f to maskColor,
                                                0.92f to maskColor.copy(alpha = 0.4f),
                                                1.00f to Color.Transparent,
                                            ),
                                    ),
                                blendMode = BlendMode.DstIn,
                            )
                        },
            ) {
                Image(
                    bitmap = thumbnailBitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

/** One radial-gradient blob's oscillation range/phase, per upstream's real `GLOW_ANIMATED` recipe. */
private data class GlowBlob(
    val xRange: ClosedFloatingPointRange<Float>,
    val xPhase: Float,
    val yRange: ClosedFloatingPointRange<Float>,
    val yPhase: Float,
    val radiusRange: ClosedFloatingPointRange<Float>,
    val radiusPhase: Float,
    val alphaStart: Float,
    val alphaEnd: Float,
)

private val GLOW_BLOBS =
    listOf(
        GlowBlob(0f..1f, 0.00f, 0f..0.5f, 0.07f, 0.8f..1.60f, 0.12f, 0.85f, 0.50f),
        GlowBlob(0f..1f, 0.20f, 0f..0.5f, 0.25f, 0.8f..1.64f, 0.18f, 0.80f, 0.45f),
        GlowBlob(0f..1f, 0.33f, 0f..0.5f, 0.36f, 0.8f..1.68f, 0.29f, 0.75f, 0.40f),
        GlowBlob(0f..1f, 0.44f, 0f..0.5f, 0.41f, 0.8f..1.72f, 0.47f, 0.70f, 0.35f),
        GlowBlob(0f..1f, 0.55f, 0f..0.5f, 0.51f, 0.8f..1.76f, 0.58f, 0.65f, 0.30f),
        GlowBlob(0f..1f, 0.66f, 0f..0.5f, 0.62f, 0.8f..1.80f, 0.69f, 0.60f, 0.25f),
    )

/** One full drift cycle, matching upstream's `GLOW_ANIMATED` (20s, linear, restart). */
private const val GLOW_CYCLE_MS = 20_000f

@Composable
private fun GlowAnimatedBackgroundLayer(
    glowSwatches: List<Color>,
    backdropColor: Color,
    isPlaying: Boolean,
    isLightTheme: Boolean,
    modifier: Modifier,
) {
    // Manually-ticked (not rememberInfiniteTransition) so pausing playback freezes the drift at
    // its current position instead of resetting to 0 — matches this fork's own established
    // "settle when paused" pattern (v0.1.13's mini-player cookie, v0.1.15's play/pause cookie).
    var progress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        var lastFrameMs = withFrameMillis { it }
        while (isActive) {
            withFrameMillis { nowMs ->
                val deltaCycles = (nowMs - lastFrameMs) / GLOW_CYCLE_MS
                lastFrameMs = nowMs
                progress = (progress + deltaCycles) % 1f
            }
        }
    }

    val swatches = glowSwatches.ifEmpty { listOf(backdropColor) }
    val baseColor = if (isLightTheme) Color(0xFFECECEC) else Color(0xFF050505)

    Box(
        modifier =
            modifier.drawWithCache {
                onDrawBehind {
                    drawRect(color = baseColor)
                    GLOW_BLOBS.forEachIndexed { index, blob ->
                        val ox = oscillate(blob.xRange, blob.xPhase, progress)
                        val oy = oscillate(blob.yRange, blob.yPhase, progress)
                        val radius = oscillate(blob.radiusRange, blob.radiusPhase, progress)
                        val color = glowColorAt(swatches, index, progress)
                        drawRect(
                            brush =
                                Brush.radialGradient(
                                    colors =
                                        listOf(
                                            color.copy(alpha = blob.alphaStart),
                                            color.copy(alpha = blob.alphaEnd),
                                            Color.Transparent,
                                        ),
                                    center = Offset(size.width * ox, size.height * oy),
                                    radius = size.width * radius,
                                ),
                        )
                    }
                }
            },
    )
}

private fun oscillate(
    range: ClosedFloatingPointRange<Float>,
    phase: Float,
    progress: Float,
): Float {
    val wave = sin(2f * PI.toFloat() * (progress + phase))
    return range.start + (range.endInclusive - range.start) * ((wave + 1f) * 0.5f)
}

/** Slowly rotates/cross-fades through [swatches] over the same cycle the blobs drift on. */
private fun glowColorAt(
    swatches: List<Color>,
    index: Int,
    progress: Float,
): Color {
    if (swatches.size == 1) return swatches[0]
    val position = (progress * swatches.size + index) % swatches.size
    val i0 = position.toInt().coerceIn(0, swatches.size - 1)
    val i1 = (i0 + 1) % swatches.size
    return lerp(swatches[i0], swatches[i1], position - i0)
}
