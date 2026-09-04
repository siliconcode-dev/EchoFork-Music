package echo.music.enhanced.crashrecovery

import android.content.Context

/**
 * Crash-safe auto-fallback for Better Echo's alternate "iOS 26 style" nav bar
 * (`AppFloatingNavBar` / the vendored `floatingtabbar/FloatingTabBar.kt`).
 *
 * v0.1.11.2's real fix (vendoring the library's source instead of consuming a
 * stale prebuilt binary) should make any crash in this specific path a thing
 * of the past — but this nav style still leans on more experimental Compose
 * APIs (SharedTransitionLayout, nested scroll collapse) than the rest of the
 * app, so this is deliberate defense-in-depth for any *future* regression
 * there, not a substitute for the real fix.
 *
 * `AppFloatingNavBar` (commonMain — can't reference this androidApp-only
 * class directly) signals "currently rendering" by writing
 * `DataStoreManager.armedIosPillNav` (true on entry, false after a short
 * grace period with no crash). [EchoMusicApplication] mirrors that Flow's
 * current value into [armed] here, a plain in-memory volatile read — instant,
 * no I/O, safe to check at crash time. The actual crash-survival write uses
 * raw [SharedPreferences] with a blocking `commit()`, not DataStore —
 * DataStore's writes are suspend/async and aren't guaranteed to flush before
 * the process is killed mid-crash. One-shot by design: a detected crash
 * forces the *next* launch onto the reliable default nav, then clears
 * itself — it never silently overwrites the user's actual saved
 * `betterEchoNavStyle` preference, so if the crash was a fluke (or is now
 * truly fixed), the chosen style resumes working normally afterwards.
 */
object NavCrashRecovery {
    private const val PREFS_NAME = "nav_crash_recovery"
    private const val KEY_FORCE_SAFE_NAV = "force_safe_nav_style"

    @Volatile
    private var appContext: Context? = null

    @Volatile
    private var armed: Boolean = false

    /** Call once, as early as possible in [android.app.Application.onCreate]. */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /** Mirrors `DataStoreManager.armedIosPillNav`'s current value — call from an Application-scope collector. */
    fun setArmedInMemory(isArmed: Boolean) {
        armed = isArmed
    }

    /**
     * Call from [cat.ereza.customactivityoncrash.CustomActivityOnCrash.EventListener.onLaunchErrorActivity],
     * which fires synchronously on the crashing thread right before the process dies. If [armed]
     * is still true (we crashed while this nav style was on screen), persist a one-shot flag
     * forcing the next launch onto the reliable default nav.
     *
     * Must never itself throw — a safety net that can cause a secondary crash is worse than none.
     */
    fun onCrash() {
        if (!armed) return
        val context = appContext ?: return
        try {
            context
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_FORCE_SAFE_NAV, true)
                .commit()
        } catch (_: Throwable) {
        }
    }

    /**
     * Call once at app startup, before the nav bar renders. One-shot: reads the flag, then
     * clears it, so this only affects the single launch right after a detected crash.
     */
    fun consumeForceSafeNavFlag(context: Context): Boolean =
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val flagged = prefs.getBoolean(KEY_FORCE_SAFE_NAV, false)
            if (flagged) {
                prefs.edit().putBoolean(KEY_FORCE_SAFE_NAV, false).commit()
            }
            flagged
        } catch (_: Throwable) {
            false
        }
}
