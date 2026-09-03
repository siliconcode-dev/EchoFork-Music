package echo.music.enhanced.data.scraper

import echo.music.enhanced.kotlinytmusicscraper.YouTube
import echo.music.enhanced.kotlinytmusicscraper.models.MediaType
import echo.music.enhanced.kotlinytmusicscraper.models.SearchSuggestions
import echo.music.enhanced.kotlinytmusicscraper.models.response.PlayerResponse
import echo.music.enhanced.kotlinytmusicscraper.pages.SearchResult

private val unsupported = Result.failure<Nothing>(UnsupportedOperationException("Innertube scraper backend is Android-only"))

internal actual suspend fun innertubeSearch(
    query: String,
    filter: YouTube.SearchFilter,
): Result<SearchResult> = unsupported

internal actual suspend fun innertubeSearchContinuation(continuation: String): Result<SearchResult> = unsupported

internal actual suspend fun innertubeSearchSuggestions(query: String): Result<SearchSuggestions> = unsupported

internal actual suspend fun innertubePlayer(
    videoId: String,
    playlistId: String?,
    noLogIn: Boolean,
): Result<Triple<String?, PlayerResponse, MediaType>> = unsupported
