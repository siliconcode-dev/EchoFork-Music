package echo.music.enhanced.di

import echo.music.enhanced.viewModel.AlbumViewModel
import echo.music.enhanced.viewModel.AnalyticsViewModel
import echo.music.enhanced.viewModel.ArtistViewModel
import echo.music.enhanced.viewModel.HomeViewModel
import echo.music.enhanced.viewModel.ImportViewModel
import echo.music.enhanced.viewModel.LibraryDynamicPlaylistViewModel
import echo.music.enhanced.viewModel.LibraryViewModel
import echo.music.enhanced.viewModel.LocalPlaylistViewModel
import echo.music.enhanced.viewModel.LogInViewModel
import echo.music.enhanced.viewModel.MoodViewModel
import echo.music.enhanced.viewModel.MoreAlbumsViewModel
import echo.music.enhanced.viewModel.NotificationViewModel
import echo.music.enhanced.viewModel.NowPlayingBottomSheetViewModel
import echo.music.enhanced.viewModel.PlaylistViewModel
import echo.music.enhanced.viewModel.PodcastViewModel
import echo.music.enhanced.viewModel.RecentlySongsViewModel
import echo.music.enhanced.viewModel.SearchViewModel
import echo.music.enhanced.viewModel.AutoEqViewModel
import echo.music.enhanced.viewModel.SettingsViewModel
import echo.music.enhanced.viewModel.SharedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val viewModelModule =
    module {
        single {
            SharedViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        single {
            SearchViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            NowPlayingBottomSheetViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LibraryViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LibraryDynamicPlaylistViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            ImportViewModel(
                get(),
            )
        }
        viewModel {
            AlbumViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            HomeViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            AutoEqViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            SettingsViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            ArtistViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            PlaylistViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LogInViewModel(
                get(),
            )
        }
        viewModel {
            PodcastViewModel(
                get(),
            )
        }
        viewModel {
            MoreAlbumsViewModel(
                get(),
            )
        }
        viewModel {
            RecentlySongsViewModel(
                get(),
            )
        }
        viewModel {
            LocalPlaylistViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            NotificationViewModel(
                get(),
            )
        }
        viewModel {
            MoodViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            AnalyticsViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
    }