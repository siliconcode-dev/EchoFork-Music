package echo.music.enhanced.data.scraper

import echo.music.enhanced.innertube.NewPipeExtractor
import echo.music.enhanced.innertube.models.YouTubeClient
import echo.music.enhanced.kotlinytmusicscraper.YouTube
import echo.music.enhanced.kotlinytmusicscraper.models.MediaType
import echo.music.enhanced.kotlinytmusicscraper.models.SearchSuggestions
import echo.music.enhanced.kotlinytmusicscraper.models.response.PlayerResponse
import echo.music.enhanced.kotlinytmusicscraper.pages.SearchResult
import kotlin.random.Random
import echo.music.enhanced.innertube.YouTube as Innertube

internal actual suspend fun innertubeSearch(
    query: String,
    filter: YouTube.SearchFilter,
): Result<SearchResult> =
    Innertube
        .search(query, Innertube.SearchFilter(filter.value))
        .map { it.toOurs() }

internal actual suspend fun innertubeSearchContinuation(continuation: String): Result<SearchResult> =
    Innertube.searchContinuation(continuation).map { it.toOurs() }

internal actual suspend fun innertubeSearchSuggestions(query: String): Result<SearchSuggestions> =
    Innertube.searchSuggestions(query).map { it.toOurs() }

internal actual suspend fun innertubePlayer(
    videoId: String,
    playlistId: String?,
    noLogIn: Boolean,
): Result<Triple<String?, PlayerResponse, MediaType>> =
    runCatching {
        val cpn =
            (1..16)
                .map { "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_"[Random.nextInt(0, 64)] }
                .joinToString("")
        val signatureTimestamp = NewPipeExtractor.getSignatureTimestamp(videoId).getOrNull()
        val response =
            Innertube
                .player(videoId, playlistId, YouTubeClient.WEB_REMIX, signatureTimestamp)
                .getOrThrow()
        val resolvedFormats =
            (response.streamingData?.formats.orEmpty() + response.streamingData?.adaptiveFormats.orEmpty())
                .associateWith { format -> NewPipeExtractor.getStreamUrl(format, videoId) }
        val mapped = response.toOurs(cpn, resolvedFormats)
        val firstThumb = mapped.videoDetails?.thumbnail?.thumbnails?.firstOrNull()
        val mediaType = if (firstThumb?.height == firstThumb?.width && firstThumb != null) MediaType.Song else MediaType.Video
        Triple(cpn, mapped, mediaType)
    }
