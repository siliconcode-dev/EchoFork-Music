package echo.music.enhanced.domain.repository

import androidx.paging.PagingData
import echo.music.enhanced.domain.data.entities.LocalPlaylistEntity
import echo.music.enhanced.domain.data.entities.PairSongLocalPlaylist
import echo.music.enhanced.domain.data.entities.SetVideoIdEntity
import echo.music.enhanced.domain.data.entities.SongEntity
import echo.music.enhanced.domain.data.model.browse.album.Track
import echo.music.enhanced.domain.data.model.browse.playlist.PlaylistState
import echo.music.enhanced.domain.utils.FilterState
import echo.music.enhanced.domain.utils.LocalResource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface LocalPlaylistRepository {
    fun getLocalPlaylist(id: Long): Flow<LocalResource<LocalPlaylistEntity?>>

    fun getAllLocalPlaylists(): Flow<List<LocalPlaylistEntity>>

    suspend fun updateLocalPlaylistTracks(
        tracks: List<String>,
        id: Long,
    )

    suspend fun updateLocalPlaylistDownloadState(
        downloadState: Int,
        id: Long,
    )

    suspend fun updateLocalPlaylistYouTubePlaylistSyncState(
        id: Long,
        syncState: Int,
    )

    suspend fun insertPairSongLocalPlaylist(pairSongLocalPlaylist: PairSongLocalPlaylist)

    fun getPlaylistPairSongByListPosition(
        playlistId: Long,
        listPosition: List<Int>,
    ): Flow<List<PairSongLocalPlaylist>?>

    fun getPlaylistPairSongByOffset(
        playlistId: Long,
        offset: Int,
        filterState: FilterState,
    ): Flow<List<PairSongLocalPlaylist>?>

    fun getPlaylistPairSongByTime(
        playlistId: Long,
        filterState: FilterState,
        localDateTime: LocalDateTime,
    ): Flow<List<PairSongLocalPlaylist>?>

    fun getPlaylistPairOfSong(
        playlistId: Long,
        videoId: String,
    ): Flow<PairSongLocalPlaylist?>

    fun changePositionOfSongInPlaylist(
        playlistId: Long,
        videoId: String,
        newPosition: Int,
    ): Flow<String>

    /**
     * Move a song within a synced playlist: updates both YouTube (via API) and local DB positions.
     * @param playlistId Local playlist ID
     * @param fromIndex The current index of the item being moved (0-based, in CustomOrder)
     * @param toIndex The target index to move the item to (0-based, in CustomOrder)
     * @return Flow<LocalResource<String>> success/error message
     */
    fun moveItemInSyncedPlaylist(
        playlistId: Long,
        fromIndex: Int,
        toIndex: Int,
    ): Flow<LocalResource<String>>

    /**
     * Move a song within a local-only (not YouTube-synced) playlist. Same shifting semantics as
     * [moveItemInSyncedPlaylist] minus the YouTube API call, so a jump between non-adjacent
     * indexes (e.g. "move to top") shifts every song in between rather than swapping just the
     * two endpoints.
     * @param playlistId Local playlist ID
     * @param fromIndex The current index of the item being moved (0-based, in CustomOrder)
     * @param toIndex The target index to move the item to (0-based, in CustomOrder)
     * @return Flow<LocalResource<String>> success/error message
     */
    fun moveItemInLocalPlaylist(
        playlistId: Long,
        fromIndex: Int,
        toIndex: Int,
    ): Flow<LocalResource<String>>

    fun downloadStateFlow(id: Long): Flow<Int>

    fun getAllDownloadingLocalPlaylists(): Flow<List<LocalPlaylistEntity>>

    fun listTrackFlow(id: Long): Flow<List<String>>

    /**
     * Search one local playlist by song title or artist name.
     *
     * A plain list, not [PagingData]: a search box already bounds its own result, and paging is
     * for a list whose end is unknown. Keeping it separate also leaves the paged reader — which
     * carries in-place reordering and removal — untouched.
     */
    fun searchTracks(
        id: Long,
        query: String,
        limit: Int = 200,
    ): Flow<List<Pair<SongEntity, PairSongLocalPlaylist>>>

    fun getTracksPaging(
        id: Long,
        filter: FilterState,
    ): Flow<PagingData<Pair<SongEntity, PairSongLocalPlaylist>>>

    suspend fun getFullPlaylistTracks(id: Long): List<SongEntity>

    suspend fun getListTrackVideoId(id: Long): List<String>

    fun insertLocalPlaylist(
        localPlaylist: LocalPlaylistEntity,
        successMessage: String,
    ): Flow<LocalResource<String>>

    /** Creates a new local playlist pre-populated with [tracks], in order. Emits the new playlist's real id. */
    fun createLocalPlaylistWithTracks(
        title: String,
        tracks: List<SongEntity>,
    ): Flow<LocalResource<Long>>

    /** Replaces every song in an existing local playlist with [tracks], in order. */
    fun replaceLocalPlaylistTracks(
        id: Long,
        tracks: List<SongEntity>,
    ): Flow<LocalResource<String>>

    fun deleteLocalPlaylist(
        id: Long,
        successMessage: String,
    ): Flow<LocalResource<String>>

    fun updateTitleLocalPlaylist(
        id: Long,
        newTitle: String,
        updatedMessage: String,
        updatedYtMessage: String,
        errorMessage: String,
    ): Flow<LocalResource<String>>

    fun updateThumbnailLocalPlaylist(
        id: Long,
        newThumbnail: String,
        successMessage: String,
    ): Flow<LocalResource<String>>

    fun updateDownloadState(
        id: Long,
        downloadState: Int,
        successMessage: String,
    ): Flow<LocalResource<String>>

    fun syncYouTubePlaylistToLocalPlaylist(
        playlist: PlaylistState,
        tracks: List<Track>,
        successMessage: String,
        errorMessage: String,
    ): Flow<LocalResource<String>>

    fun syncLocalPlaylistToYouTubePlaylist(
        playlistId: Long,
        successMessage: String,
        errorMessage: String,
    ): Flow<LocalResource<String>>

    fun unsyncLocalPlaylist(
        id: Long,
        successMessage: String,
    ): Flow<LocalResource<String>>

    fun updateSyncState(
        id: Long,
        syncState: Int,
        successMessage: String,
    ): Flow<LocalResource<String>>

    fun updateYouTubePlaylistId(
        id: Long,
        youtubePlaylistId: String,
        successMessage: String,
    ): Flow<LocalResource<String>>

    fun updateListTrackSynced(id: Long): Flow<Boolean>

    fun addTrackToLocalPlaylist(
        id: Long,
        song: SongEntity,
        successMessage: String,
        updatedYtMessage: String,
        errorMessage: String,
    ): Flow<LocalResource<String>>

    fun removeTrackFromLocalPlaylist(
        id: Long,
        song: SongEntity,
        successMessage: String,
        updatedYtMessage: String,
        errorMessage: String,
    ): Flow<LocalResource<String>>

    fun getSuggestionsTrackForPlaylist(id: Long): Flow<LocalResource<Pair<String?, List<Track>>>>

    fun reloadSuggestionPlaylist(reloadParams: String): Flow<LocalResource<Pair<String?, List<Track>>>>

    fun getYouTubeSetVideoId(youtubePlaylistId: String): Flow<List<SetVideoIdEntity>>

    fun addYouTubePlaylistItem(
        youtubePlaylistId: String,
        videoId: String,
    ): Flow<LocalResource<String>>
}