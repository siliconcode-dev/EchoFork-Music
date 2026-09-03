package echo.music.enhanced.data.scraper

import echo.music.enhanced.kotlinytmusicscraper.models.Album as OurAlbum
import echo.music.enhanced.kotlinytmusicscraper.models.AlbumItem as OurAlbumItem
import echo.music.enhanced.kotlinytmusicscraper.models.Artist as OurArtist
import echo.music.enhanced.kotlinytmusicscraper.models.ArtistItem as OurArtistItem
import echo.music.enhanced.kotlinytmusicscraper.models.PlaylistItem as OurPlaylistItem
import echo.music.enhanced.kotlinytmusicscraper.models.ResponseContext as OurResponseContext
import echo.music.enhanced.kotlinytmusicscraper.models.SearchSuggestions as OurSearchSuggestions
import echo.music.enhanced.kotlinytmusicscraper.models.SongItem as OurSongItem
import echo.music.enhanced.kotlinytmusicscraper.models.Thumbnail as OurThumbnail
import echo.music.enhanced.kotlinytmusicscraper.models.Thumbnails as OurThumbnails
import echo.music.enhanced.kotlinytmusicscraper.models.VideoItem as OurVideoItem
import echo.music.enhanced.kotlinytmusicscraper.models.WatchEndpoint as OurWatchEndpoint
import echo.music.enhanced.kotlinytmusicscraper.models.YTItem as OurYTItem
import echo.music.enhanced.kotlinytmusicscraper.models.response.PlayerResponse as OurPlayerResponse
import echo.music.enhanced.kotlinytmusicscraper.pages.SearchResult as OurSearchResult
import echo.music.enhanced.innertube.models.AlbumItem
import echo.music.enhanced.innertube.models.Artist
import echo.music.enhanced.innertube.models.ArtistItem
import echo.music.enhanced.innertube.models.PlaylistItem
import echo.music.enhanced.innertube.models.SearchSuggestions
import echo.music.enhanced.innertube.models.SongItem
import echo.music.enhanced.innertube.models.WatchEndpoint
import echo.music.enhanced.innertube.models.YTItem
import echo.music.enhanced.innertube.models.response.PlayerResponse
import echo.music.enhanced.innertube.pages.SearchResult

/**
 * Field-by-field mapping from the vendored `:innertube` module's response models (package
 * `echo.music.enhanced.innertube.*`) into this app's own `kotlinytmusicscraper.*` models, so the
 * rest of the app — parsers, repositories, UI — never needs to know which scraper backend
 * actually produced the data. Both scraper lineages mirror the same underlying YouTube Music
 * InnerTube JSON schema, so most fields line up directly; gaps are called out per type below.
 */

private fun WatchEndpoint.toOurs(): OurWatchEndpoint =
    OurWatchEndpoint(
        videoId = videoId,
        playlistId = playlistId,
        playlistSetVideoId = playlistSetVideoId,
        params = params,
        index = index,
        watchEndpointMusicSupportedConfigs =
            watchEndpointMusicSupportedConfigs?.let {
                OurWatchEndpoint.WatchEndpointMusicSupportedConfigs(
                    watchEndpointMusicConfig =
                        OurWatchEndpoint.WatchEndpointMusicSupportedConfigs.WatchEndpointMusicConfig(
                            musicVideoType = it.watchEndpointMusicConfig.musicVideoType,
                        ),
                )
            },
    )

private fun Artist.toOurs(): OurArtist = OurArtist(name = name, id = id)

/** Innertube has no distinct video-search result type — a song whose own [SongItem.isVideoSong] is true maps to [OurVideoItem] instead, to keep our `FILTER_VIDEO` search flow working. */
private fun SongItem.toOurs(): OurYTItem =
    if (isVideoSong) {
        OurVideoItem(
            id = id,
            title = title,
            thumbnail = thumbnail,
            explicit = explicit,
            endpoint = endpoint?.toOurs(),
            artists = artists.map { it.toOurs() },
            album = album?.let { OurAlbum(name = it.name, id = it.id) },
            duration = duration,
            setVideoId = setVideoId,
            musicVideoType = musicVideoType,
        )
    } else {
        OurSongItem(
            id = id,
            title = title,
            artists = artists.map { it.toOurs() },
            album = album?.let { OurAlbum(name = it.name, id = it.id) },
            duration = duration,
            thumbnail = thumbnail,
            explicit = explicit,
            endpoint = endpoint?.toOurs(),
            setVideoId = setVideoId,
            musicVideoType = musicVideoType,
        )
    }

private fun AlbumItem.toOurs(): OurAlbumItem =
    OurAlbumItem(
        browseId = browseId,
        playlistId = playlistId,
        title = title,
        artists = artists?.map { it.toOurs() },
        year = year,
        thumbnail = thumbnail,
        explicit = explicit,
    )

private fun PlaylistItem.toOurs(): OurPlaylistItem =
    OurPlaylistItem(
        id = id,
        title = title,
        author = author?.toOurs(),
        songCountText = songCountText,
        thumbnail = thumbnail.orEmpty(),
        playEndpoint = playEndpoint?.toOurs(),
        shuffleEndpoint = shuffleEndpoint?.toOurs() ?: OurWatchEndpoint(),
        radioEndpoint = radioEndpoint?.toOurs(),
    )

private fun ArtistItem.toOurs(): OurArtistItem =
    OurArtistItem(
        id = id,
        title = title,
        thumbnail = thumbnail.orEmpty(),
        shuffleEndpoint = shuffleEndpoint?.toOurs(),
        radioEndpoint = radioEndpoint?.toOurs(),
    )

/** Innertube has no podcast item type — never maps to one, so podcast search stays empty on this backend. */
private fun YTItem.toOurs(): OurYTItem? =
    when (this) {
        is SongItem -> toOurs()
        is AlbumItem -> toOurs()
        is PlaylistItem -> toOurs()
        is ArtistItem -> toOurs()
        else -> null
    }

internal fun SearchResult.toOurs(): OurSearchResult =
    OurSearchResult(
        items = items.mapNotNull { it.toOurs() },
        listPodcast = emptyList(),
        continuation = continuation,
    )

internal fun SearchSuggestions.toOurs(): OurSearchSuggestions =
    OurSearchSuggestions(
        queries = queries,
        recommendedItems = recommendedItems.mapNotNull { it.toOurs() },
    )

/**
 * [resolvedFormats] maps each raw innertube [PlayerResponse.StreamingData.Format] to its actual
 * playable URL, already deciphered via [echo.music.enhanced.innertube.NewPipeExtractor] — the
 * `signatureCipher`/throttling-parameter decoding innertube's own `player()` deliberately leaves
 * to the caller (see `NewPipe.kt`).
 *
 * innertube's model has no `hlsManifestUrl`/`serverAbrStreamingUrl` (muxed-HLS video playback
 * degrades gracefully to null on this backend) and no `captions` (unused by [innertubePlayer]'s
 * only caller, `StreamRepositoryImpl`).
 */
internal fun PlayerResponse.toOurs(
    cpn: String,
    resolvedFormats: Map<PlayerResponse.StreamingData.Format, String?>,
): OurPlayerResponse {
    fun PlayerResponse.StreamingData.Format.toOurs(): OurPlayerResponse.StreamingData.Format =
        OurPlayerResponse.StreamingData.Format(
            itag = itag,
            url = resolvedFormats[this] ?: url,
            mimeType = mimeType,
            bitrate = bitrate,
            width = width,
            height = height,
            contentLength = contentLength,
            quality = quality,
            fps = fps,
            qualityLabel = qualityLabel,
            averageBitrate = averageBitrate,
            audioQuality = audioQuality,
            approxDurationMs = approxDurationMs,
            audioSampleRate = audioSampleRate,
            audioChannels = audioChannels,
            loudnessDb = loudnessDb,
            lastModified = lastModified,
            signatureCipher = null,
        )

    return OurPlayerResponse(
        // Nothing downstream reads PlayerResponse.responseContext (confirmed by search) —
        // innertube's own type differs from ours, so a minimal stand-in is used rather than
        // mapping fields nothing consumes.
        responseContext = OurResponseContext(serviceTrackingParams = null),
        playabilityStatus = OurPlayerResponse.PlayabilityStatus(status = playabilityStatus.status, reason = playabilityStatus.reason),
        playerConfig =
            playerConfig?.let {
                OurPlayerResponse.PlayerConfig(
                    audioConfig =
                        OurPlayerResponse.PlayerConfig.AudioConfig(
                            loudnessDb = it.audioConfig.loudnessDb,
                            perceptualLoudnessDb = it.audioConfig.perceptualLoudnessDb,
                        ),
                )
            },
        streamingData =
            streamingData?.let {
                OurPlayerResponse.StreamingData(
                    hlsManifestUrl = null,
                    formats = it.formats?.map { format -> format.toOurs() },
                    adaptiveFormats = it.adaptiveFormats.map { format -> format.toOurs() },
                    expiresInSeconds = it.expiresInSeconds,
                    serverAbrStreamingUrl = null,
                )
            },
        videoDetails =
            videoDetails?.let {
                OurPlayerResponse.VideoDetails(
                    videoId = it.videoId,
                    title = it.title,
                    author = it.author,
                    channelId = it.channelId,
                    authorAvatar = null,
                    authorSubCount = null,
                    lengthSeconds = it.lengthSeconds,
                    musicVideoType = it.musicVideoType,
                    viewCount = it.viewCount,
                    thumbnail = OurThumbnails(it.thumbnail.thumbnails.map { t -> OurThumbnail(url = t.url, width = t.width, height = t.height) }),
                    description = null,
                )
            },
        playbackTracking =
            playbackTracking?.let {
                OurPlayerResponse.PlaybackTracking(
                    videostatsPlaybackUrl = it.videostatsPlaybackUrl?.let { u -> OurPlayerResponse.PlaybackTracking.VideostatsPlaybackUrl(u.baseUrl) },
                    videostatsWatchtimeUrl = it.videostatsWatchtimeUrl?.let { u -> OurPlayerResponse.PlaybackTracking.VideostatsWatchtimeUrl(u.baseUrl) },
                    atrUrl = it.atrUrl?.let { u -> OurPlayerResponse.PlaybackTracking.AtrUrl(u.baseUrl) },
                )
            },
        captions = null,
    )
}
