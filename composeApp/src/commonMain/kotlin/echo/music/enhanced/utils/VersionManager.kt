package echo.music.enhanced.utils

import echo.music.enhanced.BuildKonfig

object VersionManager {
    private var versionName: String? = null

    fun initialize() {
        if (versionName == null) {
            versionName =
                try {
                    BuildKonfig.versionName
                } catch (_: Exception) {
                    String()
                }
        }
    }

    fun getVersionName(): String = removeDevSuffix(versionName ?: String())

    /**
     * Whether [remoteTag] (e.g. a GitHub release tag like "v0.1.3") is a strictly
     * newer version than the currently installed build. Used to gate the
     * "update available" dialog so it never offers an older or identical
     * release as if it were new.
     */
    fun isRemoteVersionNewer(remoteTag: String): Boolean {
        val remote = parseVersion(remoteTag) ?: return false
        val current = parseVersion(getVersionName()) ?: return false
        for (i in 0 until maxOf(remote.size, current.size)) {
            val r = remote.getOrElse(i) { 0 }
            val c = current.getOrElse(i) { 0 }
            if (r != c) return r > c
        }
        return false
    }

    private fun parseVersion(raw: String): List<Int>? {
        val cleaned = raw.trim().removePrefix("v").removePrefix("V").substringBefore("-")
        if (cleaned.isEmpty()) return null
        val parts = cleaned.split(".")
        val numbers = parts.mapNotNull { it.toIntOrNull() }
        return if (numbers.size != parts.size) null else numbers
    }

    private fun removeDevSuffix(versionName: String): String {
        return if (versionName.endsWith("-dev")) {
            versionName.replace("-dev", "")
        } else {
            versionName
        }
    }
}