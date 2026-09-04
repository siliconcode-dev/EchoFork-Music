package echo.music.enhanced.data.di

import echo.music.enhanced.common.Config.SERVICE_SCOPE
import echo.music.enhanced.data.io.fileDir
import echo.music.enhanced.data.repository.AccountRepositoryImpl
import echo.music.enhanced.data.repository.AiPlaylistRepositoryImpl
import echo.music.enhanced.data.repository.AlbumRepositoryImpl
import echo.music.enhanced.data.repository.AnalyticsRepositoryImpl
import echo.music.enhanced.data.repository.ArtistRepositoryImpl
import echo.music.enhanced.data.repository.AutoEqRepositoryImpl
import echo.music.enhanced.data.repository.CommonRepositoryImpl
import echo.music.enhanced.data.repository.HomeRepositoryImpl
import echo.music.enhanced.data.repository.ImportRepositoryImpl
import echo.music.enhanced.data.repository.LocalPlaylistRepositoryImpl
import echo.music.enhanced.data.repository.LyricsCanvasRepositoryImpl
import echo.music.enhanced.data.repository.PlaylistRepositoryImpl
import echo.music.enhanced.data.repository.PodcastRepositoryImpl
import echo.music.enhanced.data.repository.SearchRepositoryImpl
import echo.music.enhanced.data.repository.SongRepositoryImpl
import echo.music.enhanced.data.repository.StreamRepositoryImpl
import echo.music.enhanced.data.repository.UpdateRepositoryImpl
import echo.music.enhanced.domain.repository.AccountRepository
import echo.music.enhanced.domain.repository.AiPlaylistRepository
import echo.music.enhanced.domain.repository.AlbumRepository
import echo.music.enhanced.domain.repository.AnalyticsRepository
import echo.music.enhanced.domain.repository.ArtistRepository
import echo.music.enhanced.domain.repository.AutoEqRepository
import echo.music.enhanced.domain.repository.CommonRepository
import echo.music.enhanced.domain.repository.HomeRepository
import echo.music.enhanced.domain.repository.ImportRepository
import echo.music.enhanced.domain.repository.LocalPlaylistRepository
import echo.music.enhanced.domain.repository.LyricsCanvasRepository
import echo.music.enhanced.domain.repository.PlaylistRepository
import echo.music.enhanced.domain.repository.PodcastRepository
import echo.music.enhanced.domain.repository.SearchRepository
import echo.music.enhanced.domain.repository.SongRepository
import echo.music.enhanced.domain.repository.StreamRepository
import echo.music.enhanced.domain.repository.UpdateRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AccountRepository>(createdAtStart = true) {
            AccountRepositoryImpl(get(), get())
        }

        single<AiPlaylistRepository>(createdAtStart = true) {
            AiPlaylistRepositoryImpl(get(), get(), get(), get(), get())
        }

        single<AlbumRepository>(createdAtStart = true) {
            AlbumRepositoryImpl(get(), get())
        }

        single<ArtistRepository>(createdAtStart = true) {
            ArtistRepositoryImpl(get(), get(), get())
        }

        single<CommonRepository>(createdAtStart = true) {
            CommonRepositoryImpl(get(named(SERVICE_SCOPE)), get(), get(), get(), get(), get()).apply {
                this.init("${fileDir()}/ytdlp-cookie.txt", get())
            }
        }

        // Lazy for the same reason its client is: the picker is the only thing that wants it.
        single<AutoEqRepository> {
            AutoEqRepositoryImpl(get(), get())
        }

        single<HomeRepository>(createdAtStart = true) {
            HomeRepositoryImpl(get(), get())
        }

        single<ImportRepository>(createdAtStart = true) {
            ImportRepositoryImpl(get())
        }

        single<LocalPlaylistRepository>(createdAtStart = true) {
            LocalPlaylistRepositoryImpl(get(), get())
        }

        single<LyricsCanvasRepository>(createdAtStart = true) {
            LyricsCanvasRepositoryImpl(get(), get(), get(), get(), get())
        }

        single<PlaylistRepository>(createdAtStart = true) {
            PlaylistRepositoryImpl(get(), get(), get())
        }

        single<PodcastRepository>(createdAtStart = true) {
            PodcastRepositoryImpl(get(), get())
        }

        single<SearchRepository>(createdAtStart = true) {
            SearchRepositoryImpl(get(), get())
        }

        single<SongRepository>(createdAtStart = true) {
            SongRepositoryImpl(get(), get(), get())
        }

        single<StreamRepository>(createdAtStart = true) {
            StreamRepositoryImpl(get(), get())
        }

        single<UpdateRepository>(createdAtStart = true) {
            UpdateRepositoryImpl(get())
        }

        single<AnalyticsRepository>(createdAtStart = true) {
            AnalyticsRepositoryImpl(get())
        }
    }