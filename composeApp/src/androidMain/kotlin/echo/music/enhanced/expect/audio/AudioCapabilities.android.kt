package echo.music.enhanced.expect.audio

import android.content.Intent
import android.media.AudioManager
import android.media.audiofx.AudioEffect
import android.os.Build
import android.provider.Settings
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

@Composable
actual fun rememberOpenSoundSettingsAction(): () -> Unit {
    val context = LocalContext.current
    return {
        try {
            context.startActivity(Intent(Settings.ACTION_SOUND_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (_: Exception) {
            // No Sound settings screen on this device/ROM — nothing sensible to fall back to.
        }
    }
}
