package echo.music.enhanced.data.scraper

import echo.music.enhanced.kotlinytmusicscraper.YouTube
import echo.music.enhanced.kotlinytmusicscraper.models.MediaType
import echo.music.enhanced.kotlinytmusicscraper.models.SearchSuggestions
import echo.music.enhanced.kotlinytmusicscraper.models.response.PlayerResponse
import echo.music.enhanced.kotlinytmusicscraper.pages.SearchResult

/**
 * Bridge to the androidMain-only `:innertube` module (vendored from upstream Echo Music, see
 * `core/service/innertube`), used by [InnertubeAdapter] for the two Innertube-backed methods.
 * jvmMain always fails — this scraper backend is Android-only in practice, same as
 * `NewLyricsProviders`/`NewCanvasProviders`.
 */
internal expect suspend fun innertubeSearch(
    query: String,
    filter: YouTube.SearchFilter,
): Result<SearchResult>

internal expect suspend fun innertubeSearchContinuation(continuation: String): Result<SearchResult>

internal expect suspend fun innertubeSearchSuggestions(query: String): Result<SearchSuggestions>

internal expect suspend fun innertubePlayer(
    videoId: String,
    playlistId: String?,
    noLogIn: Boolean,
): Result<Triple<String?, PlayerResponse, MediaType>>
