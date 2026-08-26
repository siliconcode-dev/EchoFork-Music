package echo.music.enhanced.data.mediaservice

import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.domain.mediaservice.handler.MediaPlayerHandler
import echo.music.enhanced.domain.repository.AnalyticsRepository
import echo.music.enhanced.domain.repository.LocalPlaylistRepository
import echo.music.enhanced.domain.repository.SongRepository
import echo.music.enhanced.domain.repository.StreamRepository
import kotlinx.coroutines.CoroutineScope

expect fun createMediaServiceHandler(
    dataStoreManager: DataStoreManager,
    songRepository: SongRepository,
    streamRepository: StreamRepository,
    localPlaylistRepository: LocalPlaylistRepository,
    analyticsRepository: AnalyticsRepository,
    coroutineScope: CoroutineScope,
): MediaPlayerHandler