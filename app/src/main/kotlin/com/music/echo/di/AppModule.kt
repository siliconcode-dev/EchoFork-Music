

package echo.music.enhanced.di

import android.content.Context
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.room.Room
import echo.music.enhanced.constants.MaxSongCacheSizeKey
import echo.music.enhanced.db.InternalDatabase
import echo.music.enhanced.db.MusicDatabase
import echo.music.enhanced.utils.dataStore
import echo.music.enhanced.utils.get
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @Provides
    fun provideDao(
        database: InternalDatabase,
    ) = database.dao


    @Singleton
    @Provides
    fun provideDatabase(
        internalDatabase: InternalDatabase,
    ): MusicDatabase = MusicDatabase(internalDatabase)

    @Singleton
    @Provides
    fun provideInternalDatabase(
        @ApplicationContext context: Context,
    ): InternalDatabase = Room
        .databaseBuilder(context, InternalDatabase::class.java, InternalDatabase.DB_NAME)
        .addMigrations(
            echo.music.enhanced.db.MIGRATION_1_2,
            echo.music.enhanced.db.MIGRATION_21_24,
            echo.music.enhanced.db.MIGRATION_22_24,
            echo.music.enhanced.db.MIGRATION_24_25,
            echo.music.enhanced.db.MIGRATION_27_28,
            echo.music.enhanced.db.MIGRATION_28_29,
            echo.music.enhanced.db.MIGRATION_29_30,
            echo.music.enhanced.db.MIGRATION_31_32,
            echo.music.enhanced.db.MIGRATION_36_37,
            echo.music.enhanced.db.MIGRATION_37_38,
            echo.music.enhanced.db.MIGRATION_38_39,
            echo.music.enhanced.db.MIGRATION_39_40,
            echo.music.enhanced.db.MIGRATION_40_41,
            echo.music.enhanced.db.MIGRATION_41_42,
            echo.music.enhanced.db.MIGRATION_42_43,
            echo.music.enhanced.db.MIGRATION_43_44,
        )
        .setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
        .setTransactionExecutor(java.util.concurrent.Executors.newFixedThreadPool(4))
        .setQueryExecutor(java.util.concurrent.Executors.newFixedThreadPool(4))
        .addCallback(object : androidx.room.RoomDatabase.Callback() {
            override fun onOpen(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                super.onOpen(db)
                try {
                    db.query("PRAGMA busy_timeout = 60000").close()
                    db.query("PRAGMA cache_size = -16000").close()
                    db.query("PRAGMA wal_autocheckpoint = 1000").close()
                    db.query("PRAGMA synchronous = NORMAL").close()
                } catch (e: Exception) {
                    timber.log.Timber.tag("MusicDatabase").e(e, "Failed to set PRAGMA settings")
                }
            }
        })
        .build()

    @Singleton
    @Provides
    fun provideDatabaseProvider(
        @ApplicationContext context: Context,
    ): DatabaseProvider = StandaloneDatabaseProvider(context)

    @Singleton
    @Provides
    @PlayerCache
    fun providePlayerCache(
        @ApplicationContext context: Context,
        databaseProvider: DatabaseProvider,
    ): SimpleCache {
        val cacheSize = context.dataStore[MaxSongCacheSizeKey] ?: 1024
        return SimpleCache(
            context.filesDir.resolve("exoplayer"),
            when (cacheSize) {
                -1 -> NoOpCacheEvictor()
                else -> LeastRecentlyUsedCacheEvictor(cacheSize * 1024 * 1024L)
            },
            databaseProvider,
        )
    }

    @Singleton
    @Provides
    @DownloadCache
    fun provideDownloadCache(
        @ApplicationContext context: Context,
        databaseProvider: DatabaseProvider,
    ): SimpleCache {
        return SimpleCache(
            context.filesDir.resolve("download"),
            NoOpCacheEvictor(),
            databaseProvider
        )
    }

    @Singleton
    @Provides
        @ApplicationContext context: Context,

    @Singleton
    @Provides
        @ApplicationContext context: Context,
}
