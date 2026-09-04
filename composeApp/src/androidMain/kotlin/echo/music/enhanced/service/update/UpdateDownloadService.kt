package echo.music.enhanced.service.update

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import echo.music.enhanced.domain.data.model.update.UpdateDownloadState
import echo.music.enhanced.domain.manager.UpdateDownloadManager
import echo.music.enhanced.domain.repository.UpdateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import java.io.File

/**
 * Foreground service so the APK download survives the app being backgrounded or closed — mirrors
 * the existing song-download feature's own foreground-service approach, but as a plain Service
 * (not Media3's DownloadManager, which only understands media downloads) around the same
 * Ktor/Okio chunked-download primitive [UpdateRepository.downloadApk] already uses.
 */
class UpdateDownloadService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val updateRepository: UpdateRepository = getKoin().get()
    private val updateDownloadManager: UpdateDownloadManager = getKoin().get()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        val downloadUrl = intent?.getStringExtra(EXTRA_DOWNLOAD_URL)
        val versionTag = intent?.getStringExtra(EXTRA_VERSION_TAG)
        val installImmediately = intent?.getBooleanExtra(EXTRA_INSTALL_IMMEDIATELY, false) ?: false
        if (downloadUrl == null || versionTag == null) {
            stopSelf(startId)
            return START_NOT_STICKY
        }

        UpdateNotifications.createChannel(applicationContext)
        startForeground(UpdateNotifications.PROGRESS_NOTIFICATION_ID, UpdateNotifications.buildProgressNotification(applicationContext, 0f))

        val apkFile = File(applicationContext.getExternalFilesDir(null), "update-$versionTag.apk")
        serviceScope.launch {
            runCatching {
                updateRepository.downloadApk(downloadUrl, apkFile.absolutePath).collect { (isDone, progress, speedKbps) ->
                    if (!isDone) {
                        updateDownloadManager.setState(UpdateDownloadState.Downloading(progress, speedKbps))
                        NotificationManagerCompat
                            .from(applicationContext)
                            .notify(
                                UpdateNotifications.PROGRESS_NOTIFICATION_ID,
                                UpdateNotifications.buildProgressNotification(applicationContext, progress),
                            )
                    } else if (progress >= 1f) {
                        onDownloadReady(apkFile, versionTag, installImmediately)
                    } else {
                        onDownloadFailed("Download did not complete")
                    }
                }
            }.onFailure { onDownloadFailed(it.localizedMessage ?: "Download error") }
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf(startId)
        }
        return START_NOT_STICKY
    }

    private fun onDownloadReady(
        apkFile: File,
        versionTag: String,
        installImmediately: Boolean,
    ) {
        updateDownloadManager.setState(UpdateDownloadState.ReadyToInstall(apkFile.absolutePath))
        if (installImmediately && applicationContext.packageManager.canRequestPackageInstalls()) {
            startActivity(UpdateNotifications.apkInstallIntent(applicationContext, apkFile))
        } else {
            UpdateNotifications.notifyReadyToInstall(applicationContext, apkFile)
        }
    }

    private fun onDownloadFailed(message: String) {
        updateDownloadManager.setState(UpdateDownloadState.Failed(message))
        UpdateNotifications.notifyFailed(applicationContext, message)
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_DOWNLOAD_URL = "download_url"
        const val EXTRA_VERSION_TAG = "version_tag"
        const val EXTRA_INSTALL_IMMEDIATELY = "install_immediately"
    }
}
