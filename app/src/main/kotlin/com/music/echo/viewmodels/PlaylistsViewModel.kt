

@file:OptIn(ExperimentalCoroutinesApi::class)

package echo.music.enhanced.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import echo.music.enhanced.constants.AddToPlaylistSortDescendingKey
import echo.music.enhanced.constants.AddToPlaylistSortTypeKey
import echo.music.enhanced.constants.PlaylistSortType
import echo.music.enhanced.db.MusicDatabase
import echo.music.enhanced.extensions.toEnum
import echo.music.enhanced.utils.SyncUtils
import echo.music.enhanced.utils.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel
@Inject
constructor(
    @ApplicationContext context: Context,
    database: MusicDatabase,
    private val syncUtils: SyncUtils,
) : ViewModel() {
    val allPlaylists =
        context.dataStore.data
            .map {
                it[AddToPlaylistSortTypeKey].toEnum(PlaylistSortType.CREATE_DATE) to (it[AddToPlaylistSortDescendingKey]
                    ?: true)
            }.distinctUntilChanged()
            .flatMapLatest { (sortType, descending) ->
                database.playlists(sortType, descending)
            }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    
    suspend fun sync() {
        syncUtils.syncSavedPlaylists()
    }
}
