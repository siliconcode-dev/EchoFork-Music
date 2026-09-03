package echo.music.enhanced.kotlinytmusicscraper

import echo.music.enhanced.kotlinytmusicscraper.models.AccountInfo
import echo.music.enhanced.kotlinytmusicscraper.models.SongItem
import echo.music.enhanced.kotlinytmusicscraper.models.SearchSuggestions
import echo.music.enhanced.kotlinytmusicscraper.models.SongInfo
import echo.music.enhanced.kotlinytmusicscraper.models.WatchEndpoint
import echo.music.enhanced.kotlinytmusicscraper.models.YouTubeLocale
import echo.music.enhanced.kotlinytmusicscraper.models.MediaType
import echo.music.enhanced.kotlinytmusicscraper.models.MusicTwoRowItemRenderer
import echo.music.enhanced.kotlinytmusicscraper.models.response.AddItemYouTubePlaylistResponse
import echo.music.enhanced.kotlinytmusicscraper.models.response.BrowseResponse
import echo.music.enhanced.kotlinytmusicscraper.models.response.CreatePlaylistResponse
import echo.music.enhanced.kotlinytmusicscraper.models.response.DownloadProgress
import echo.music.enhanced.kotlinytmusicscraper.models.response.LikeStatus
import echo.music.enhanced.kotlinytmusicscraper.models.response.PlayerResponse
import echo.music.enhanced.kotlinytmusicscraper.models.response.SimpMusicChartResponse
import echo.music.enhanced.kotlinytmusicscraper.models.TidalMetadataResult
import echo.music.enhanced.kotlinytmusicscraper.models.response.RemoteConfig
import echo.music.enhanced.kotlinytmusicscraper.models.simpmusic.FdroidResponse
import echo.music.enhanced.kotlinytmusicscraper.models.simpmusic.GithubResponse
import echo.music.enhanced.kotlinytmusicscraper.models.sponsorblock.SkipSegments
import echo.music.enhanced.kotlinytmusicscraper.models.youtube.Transcript
import echo.music.enhanced.kotlinytmusicscraper.models.youtube.YouTubeInitialPage
import echo.music.enhanced.kotlinytmusicscraper.pages.AlbumPage
import echo.music.enhanced.kotlinytmusicscraper.pages.ArtistPage
import echo.music.enhanced.kotlinytmusicscraper.pages.BrowseResult
import echo.music.enhanced.kotlinytmusicscraper.pages.ExplorePage
import echo.music.enhanced.kotlinytmusicscraper.pages.MoodAndGenres
import echo.music.enhanced.kotlinytmusicscraper.pages.NextResult
import echo.music.enhanced.kotlinytmusicscraper.pages.SearchResult
import kotlinx.coroutines.flow.Flow
import okio.Path

/**
 * Shared surface for the two scraper backends ([YouTube], the default, and the
 * Innertube-backed adapter). Covers exactly the methods/properties the `core/data`
 * repositories actually call — not either backend's full API. Signatures copied
 * verbatim from [YouTube] so it can implement this with zero behavior change.
 */
interface YtMusicScraper {
    var cookiePath: Path?
    var locale: YouTubeLocale
    var visitorData: String?
    var dataSyncId: String?
    var cookie: String?
    var pageId: String?
    var tidalClientId: String
    var tidalClientSecret: String

    suspend fun visitorData(): String?

    fun removeProxy()

    fun setProxy(
        isHttp: Boolean,
        host: String,
        port: Int,
    )

    suspend fun search(
        query: String,
        filter: YouTube.SearchFilter,
    ): Result<SearchResult>

    suspend fun searchContinuation(continuation: String): Result<SearchResult>

    suspend fun getYTMusicSearchSuggestions(query: String): Result<SearchSuggestions>

    suspend fun album(
        browseId: String,
        withSongs: Boolean = true,
    ): Result<AlbumPage>

    suspend fun browse(
        browseId: String,
        params: String?,
    ): Result<BrowseResult>

    suspend fun customQuery(
        browseId: String?,
        params: String? = null,
        continuation: String? = null,
        country: String? = null,
        setLogin: Boolean = true,
    ): Result<BrowseResponse>

    suspend fun newRelease(): Result<ExplorePage>

    suspend fun moodAndGenres(): Result<List<MoodAndGenres>>

    suspend fun next(
        endpoint: WatchEndpoint,
        continuation: String? = null,
    ): Result<NextResult>

    suspend fun getLibraryPlaylists(): Result<BrowseResponse>

    suspend fun nextYouTubePlaylists(continuation: String): Result<Pair<List<MusicTwoRowItemRenderer>, String?>>

    suspend fun getMixedForYou(): Result<BrowseResponse>

    suspend fun editPlaylist(
        playlistId: String,
        title: String,
    ): Result<Int>

    suspend fun createPlaylist(
        title: String,
        listVideoId: List<String>?,
    ): Result<CreatePlaylistResponse>

    suspend fun getSimpMusicChart(): Result<SimpMusicChartResponse>

    suspend fun getSongInfo(videoId: String): Result<SongInfo>

    suspend fun artist(browseId: String): Result<ArtistPage>

    suspend fun getYouTubePlaylistFullTracksWithSetVideoId(playlistId: String): Result<List<Pair<SongItem, String>>>

    suspend fun getSuggestionsTrackForPlaylist(playlistId: String): Result<Pair<String?, List<SongItem>?>?>

    suspend fun addPlaylistItem(
        playlistId: String,
        videoId: String,
    ): Result<AddItemYouTubePlaylistResponse>

    suspend fun movePlaylistItem(
        playlistId: String,
        setVideoId: String,
        movedSetVideoIdSuccessor: String? = null,
    ): Result<Int>

    suspend fun removeItemYouTubePlaylist(
        playlistId: String,
        videoId: String,
        setVideoId: String,
    ): Result<Int>

    suspend fun getAccountListWithPageId(customCookie: String): Result<List<AccountInfo>>

    suspend fun getLikedInfo(videoId: String): Result<LikeStatus>

    suspend fun addToLiked(mediaId: String): Result<Int>

    suspend fun removeFromLiked(mediaId: String): Result<Int>

    suspend fun subscribeChannel(channelId: String): Result<Int>

    suspend fun unsubscribeChannel(channelId: String): Result<Int>

    suspend fun getYouTubeCaption(
        videoId: String,
        preferLang: String,
    ): Result<Pair<Transcript, Transcript?>>

    suspend fun checkForGithubReleaseUpdate(): Result<GithubResponse>

    suspend fun checkForFdroidUpdate(): Result<FdroidResponse>

    suspend fun getTidalRemoteConfig(): Result<RemoteConfig>

    suspend fun searchTidalMetadata(
        query: String,
        durationSeconds: Int,
    ): Result<TidalMetadataResult>

    suspend fun initPlayback(
        playbackUrl: String,
        atrUrl: String,
        watchtimeUrl: String,
        cpn: String,
        playlistId: String?,
    ): Result<Pair<Int, Float>>

    suspend fun updateWatchTime(
        watchtimeUrl: String,
        watchtimeList: ArrayList<Float>,
        cpn: String,
        playlistId: String?,
    ): Result<Int>

    suspend fun updateWatchTimeFull(
        watchtimeUrl: String,
        cpn: String,
        playlistId: String?,
    ): Result<Int>

    suspend fun getSkipSegments(videoId: String): Result<List<SkipSegments>>

    suspend fun getFullMetadata(videoId: String): Result<YouTubeInitialPage>

    fun isManifestUrl(url: String): Boolean

    suspend fun is403Url(url: String): Boolean

    suspend fun player(
        videoId: String,
        playlistId: String? = null,
        noLogIn: Boolean = false,
    ): Result<Triple<String?, PlayerResponse, MediaType>>

    fun download(
        track: SongItem,
        filePath: String,
        videoId: String,
        isVideo: Boolean = false,
    ): Flow<DownloadProgress>
}
