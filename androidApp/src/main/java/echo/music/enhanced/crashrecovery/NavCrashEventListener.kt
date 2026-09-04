package echo.music.enhanced.crashrecovery

import cat.ereza.customactivityoncrash.CustomActivityOnCrash

/**
 * Top-level (not inner/anonymous) per CustomActivityOnCrash's requirement that an
 * [CustomActivityOnCrash.EventListener] be [java.io.Serializable]. Stateless — reads/writes go
 * through [NavCrashRecovery]'s own stored application context, not anything captured here.
 */
class NavCrashEventListener : CustomActivityOnCrash.EventListener {
    override fun onLaunchErrorActivity() {
        NavCrashRecovery.onCrash()
    }

    override fun onRestartAppFromErrorActivity() {}

    override fun onCloseAppFromErrorActivity() {}
}
