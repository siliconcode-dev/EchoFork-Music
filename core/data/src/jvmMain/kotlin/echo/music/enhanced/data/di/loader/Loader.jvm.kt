package echo.music.enhanced.data.di.loader

import echo.music.enhanced.media_jvm.di.loadDesktopPlayerModule

actual fun loadMediaService() {
    loadDesktopPlayerModule()
}
