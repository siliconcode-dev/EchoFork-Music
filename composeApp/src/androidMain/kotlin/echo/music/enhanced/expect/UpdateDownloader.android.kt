package echo.music.enhanced.expect

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import echo.music.enhanced.domain.data.model.update.UpdateAsset
import echo.music.enhanced.service.update.UpdateDownloadService
import echo.music.enhanced.service.update.UpdateNotifications
import org.koin.mp.KoinPlatform.getKoin
import java.io.File

actual fun startApkUpdateDownload(
    assets: List<UpdateAsset>,
    versionTag: String,
    installImmediately: Boolean,
) {
    val asset = pickAssetForDevice(assets) ?: return
    val context: AppCompatActivity = getKoin().get()
    val intent =
        Intent(context, UpdateDownloadService::class.java).apply {
            putExtra(UpdateDownloadService.EXTRA_DOWNLOAD_URL, asset.downloadUrl)
            putExtra(UpdateDownloadService.EXTRA_VERSION_TAG, versionTag)
            putExtra(UpdateDownloadService.EXTRA_INSTALL_IMMEDIATELY, installImmediately)
        }
    ContextCompat.startForegroundService(context, intent)
}

actual fun installDownloadedApk(apkPath: String) {
    val context: AppCompatActivity = getKoin().get()
    val intent = UpdateNotifications.apkInstallIntent(context, File(apkPath)).apply { addFlags(FLAG_ACTIVITY_NEW_TASK) }
    context.startActivity(intent)
}

/** Matches the device's own ABI list against the 4 real release asset names, falling back to universal. */
private fun pickAssetForDevice(assets: List<UpdateAsset>): UpdateAsset? {
    if (assets.isEmpty()) return null
    for (abi in Build.SUPPORTED_ABIS) {
        assets.firstOrNull { it.name.contains(abi, ignoreCase = true) }?.let { return it }
    }
    return assets.firstOrNull { it.name.contains("universal", ignoreCase = true) } ?: assets.firstOrNull()
}
