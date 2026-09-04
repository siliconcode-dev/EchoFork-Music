package echo.music.enhanced.expect

import echo.music.enhanced.domain.data.model.update.UpdateAsset

/**
 * Starts the platform's real APK download for an in-app update. The actual implementation picks
 * the right asset for the current device out of [assets] (e.g. matching Android's ABI list) —
 * callers just hand over everything the release had.
 */
expect fun startApkUpdateDownload(
    assets: List<UpdateAsset>,
    versionTag: String,
    installImmediately: Boolean,
)

/** Launches the system package installer for an already-downloaded update APK at [apkPath]. */
expect fun installDownloadedApk(apkPath: String)
