package echo.music.enhanced.expect

import androidx.compose.runtime.Composable

expect fun copyToClipboard(
    label: String,
    text: String,
)