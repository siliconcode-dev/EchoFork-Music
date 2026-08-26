package echo.music.enhanced.data.mediaservice

actual fun createMediaServiceHandler(
    dataStoreManager: echo.music.enhanced.domain.manager.DataStoreManager,
    songRepository: echo.music.enhanced.domain.repository.SongRepository,
    streamRepository: echo.music.enhanced.domain.repository.StreamRepository,
    localPlaylistRepository: echo.music.enhanced.domain.repository.LocalPlaylistRepository,
    analyticsRepository: echo.music.enhanced.domain.repository.AnalyticsRepository,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
): echo.music.enhanced.domain.mediaservice.handler.MediaPlayerHandler {
    TODO("Not yet implemented")
}