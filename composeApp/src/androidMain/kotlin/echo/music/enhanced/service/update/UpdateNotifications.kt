package echo.music.enhanced.service.update

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * Own channel for the in-app updater — distinct from the existing song-download feature's
 * "download" channel id, since these are semantically different notifications.
 */
object UpdateNotifications {
    private const val CHANNEL_ID = "app_update_channel"
    const val PROGRESS_NOTIFICATION_ID = 5001
    const val READY_NOTIFICATION_ID = 5002
    const val FAILED_NOTIFICATION_ID = 5003

    fun createChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel =
                NotificationChannel(CHANNEL_ID, "App update", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "Download progress and install prompts for app updates"
                }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun buildProgressNotification(
        context: Context,
        progress: Float,
    ): android.app.Notification =
        NotificationCompat
            .Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Downloading update")
            .setProgress(100, (progress * 100).toInt().coerceIn(0, 100), false)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

    fun apkInstallIntent(
        context: Context,
        apkFile: File,
    ): Intent {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.FileProvider", apkFile)
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
    }

    fun unknownAppSourcesIntent(context: Context): Intent =
        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:${context.packageName}"))
            .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

    private fun canPostNotifications(context: Context): Boolean =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    fun notifyReadyToInstall(
        context: Context,
        apkFile: File,
    ) {
        if (!canPostNotifications(context)) return
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                READY_NOTIFICATION_ID,
                apkInstallIntent(context, apkFile),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
        val notification =
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Update ready to install")
                .setContentText("Tap to install the new version")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        NotificationManagerCompat.from(context).notify(READY_NOTIFICATION_ID, notification)
    }

    fun notifyFailed(
        context: Context,
        message: String,
    ) {
        if (!canPostNotifications(context)) return
        val notification =
            NotificationCompat
                .Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .setContentTitle("Update failed")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()
        NotificationManagerCompat.from(context).notify(FAILED_NOTIFICATION_ID, notification)
    }
}
