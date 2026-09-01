package echo.music.enhanced.expect.audio

import android.media.AudioManager
import android.media.audiofx.AudioEffect
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun isSpatialAudioSupported(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return false
    val context = LocalContext.current
    val audioManager = context.getSystemService(AudioManager::class.java) ?: return false
    return audioManager.spatializer.isAvailable
}

@Composable
actual fun isOemDolbyEngineDetected(): Boolean {
    val descriptors =
        try {
            AudioEffect.queryEffects()
        } catch (_: Exception) {
            null
        }
    return descriptors.orEmpty().any { it.name?.contains("dolby", ignoreCase = true) == true }
}
