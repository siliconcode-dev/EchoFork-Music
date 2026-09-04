package echo.music.enhanced.domain.data.model.update

/** Live state of the in-app APK updater, shared between the Android download service and the UI. */
sealed class UpdateDownloadState {
    data object Idle : UpdateDownloadState()

    data class Downloading(
        val progress: Float,
        val speedKbps: Int,
    ) : UpdateDownloadState()

    data class ReadyToInstall(
        val apkPath: String,
    ) : UpdateDownloadState()

    data class Failed(
        val message: String,
    ) : UpdateDownloadState()
}
