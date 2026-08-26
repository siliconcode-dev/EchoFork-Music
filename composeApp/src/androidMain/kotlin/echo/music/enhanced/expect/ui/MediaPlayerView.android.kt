package echo.music.enhanced.expect.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import echo.music.enhanced.domain.data.model.metadata.Lyrics
import echo.music.enhanced.domain.data.model.streams.TimeLine
import echo.music.enhanced.media3.ui.MediaPlayerView
import echo.music.enhanced.media3.ui.MediaPlayerViewWithSubtitle
import echo.music.enhanced.extension.findActivity
import echo.music.enhanced.extension.getScreenSizeInfo
import echo.music.enhanced.ui.theme.typo

@Composable
actual fun MediaPlayerView(
    url: String,
    modifier: Modifier,
    cropToBounds: Boolean,
) {
    MediaPlayerView(
        modifier = modifier,
        context = LocalContext.current,
        density = LocalDensity.current,
        url = url,
        screenSize = getScreenSizeInfo(),
        cropToBounds = cropToBounds,
    )
}

@Composable
actual fun MediaPlayerViewWithSubtitle(
    modifier: Modifier,
    playerName: String,
    shouldPip: Boolean,
    shouldShowSubtitle: Boolean,
    shouldScaleDownSubtitle: Boolean,
    isInPipMode: Boolean,
    timelineState: TimeLine,
    lyricsData: Lyrics?,
    translatedLyricsData: Lyrics?,
    mainTextStyle: TextStyle,
    translatedTextStyle: TextStyle,
) {
    MediaPlayerViewWithSubtitle(
        playerName = playerName,
        modifier = modifier,
        shouldShowSubtitle = shouldShowSubtitle,
        shouldPip = shouldPip,
        shouldScaleDownSubtitle = shouldScaleDownSubtitle,
        timelineState = timelineState,
        lyricsData = lyricsData,
        translatedLyricsData = translatedLyricsData,
        context = LocalContext.current,
        activity = LocalActivity.current as? ComponentActivity ?: LocalContext.current.findActivity(),
        isInPipMode = isInPipMode,
        mainTextStyle = typo().bodyLarge,
        translatedTextStyle = typo().bodyMedium,
    )
}