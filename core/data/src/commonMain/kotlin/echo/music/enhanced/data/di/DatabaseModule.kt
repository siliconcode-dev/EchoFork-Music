package echo.music.enhanced.data.di

import DatabaseDao
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import echo.music.enhanced.data.dataStore.DataStoreManagerImpl
import echo.music.enhanced.data.dataStore.createDataStoreInstance
import echo.music.enhanced.data.db.Converters
import echo.music.enhanced.data.db.MusicDatabase
import echo.music.enhanced.data.db.datasource.AnalyticsDatasource
import echo.music.enhanced.data.db.datasource.LocalDataSource
import echo.music.enhanced.data.db.getDatabaseBuilder
import echo.music.enhanced.data.scraper.InnertubeAdapter
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.domain.manager.DataStoreManager.Values.INNERTUBE
import echo.music.enhanced.kotlinytmusicscraper.YouTube
import echo.music.enhanced.kotlinytmusicscraper.YtMusicScraper
import echo.music.enhanced.spotify.Spotify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import echo.music.enhanced.aiservice.AiClient
import echo.music.enhanced.lyrics.SimpMusicLyricsClient
import kotlin.time.ExperimentalTime
import echo.music.enhanced.autoeq.AutoEq

@OptIn(ExperimentalTime::class)
val databaseModule =
    module {
        single(createdAtStart = true) {
            Converters()
        }
        // Database
        single(createdAtStart = true) {
            getDatabaseBuilder(
                get<Converters>()
            )
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
        // DatabaseDao
        single(createdAtStart = true) {
            get<MusicDatabase>().getDatabaseDao()
        }
        // LocalDataSource
        single(createdAtStart = true) {
            LocalDataSource(get<DatabaseDao>(), get<MusicDatabase>())
        }
        // AnalyticsDatasource
        single(createdAtStart = true) {
            AnalyticsDatasource(get<DatabaseDao>())
        }
        // Datastore
        single(createdAtStart = true) {
            createDataStoreInstance()
        }
        // DatastoreManager
        single<DataStoreManager>(createdAtStart = true) {
            DataStoreManagerImpl(get<DataStore<Preferences>>())
        }

        // Move YouTube from Singleton to Koin DI
        single<YtMusicScraper>(createdAtStart = true) {
            val youTube = YouTube()
            // One-time synchronous read at process start — the scraper backend can't be swapped
            // live (it's constructor-injected into 13 already-built repository singletons), so
            // switching in Settings takes effect after restart. See InnertubeAdapter's kdoc.
            val backend = runBlocking { get<DataStoreManager>().scraperBackend.first() }
            if (backend == INNERTUBE) InnertubeAdapter(youTube) else youTube
        }

        single(createdAtStart = true) {
            Spotify()
        }

        single(createdAtStart = true) {
            AiClient()
        }

        single(createdAtStart = true) {
            SimpMusicLyricsClient()
        }

        // Not created at start, unlike the rest: nothing needs it until someone opens the AutoEq
        // picker, and it holds an HTTP client the vast majority of sessions never use.
        single {
            AutoEq()
        }
    }