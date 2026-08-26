package echo.music.enhanced.data.mediaservice

import echo.music.enhanced.domain.repository.AnalyticsRepository

actual fun createMediaServiceHandler(
    dataStoreManager: echo.music.enhanced.domain.manager.DataStoreManager,
    songRepository: echo.music.enhanced.domain.repository.SongRepository,
    streamRepository: echo.music.enhanced.domain.repository.StreamRepository,
    localPlaylistRepository: echo.music.enhanced.domain.repository.LocalPlaylistRepository,
    analyticsRepository: AnalyticsRepository,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
): echo.music.enhanced.domain.mediaservice.handler.MediaPlayerHandler =
    MediaServiceHandlerImpl(
        dataStoreManager,
        songRepository,
        streamRepository,
        localPlaylistRepository,
        analyticsRepository,
        coroutineScope,
    )