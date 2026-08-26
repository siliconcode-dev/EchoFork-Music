package echo.music.enhanced.data.di.loader

import echo.music.enhanced.data.di.databaseModule
import echo.music.enhanced.data.di.mediaHandlerModule
import echo.music.enhanced.data.di.repositoryModule
import org.koin.core.context.loadKoinModules

fun loadAllModules() {
    loadKoinModules(
        listOf(
            databaseModule,
            repositoryModule,
        ),
    )
    loadKoinModules(mediaHandlerModule)
    loadMediaService()
}

expect fun loadMediaService()