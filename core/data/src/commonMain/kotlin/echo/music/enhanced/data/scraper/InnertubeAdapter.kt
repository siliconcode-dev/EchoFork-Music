package echo.music.enhanced.data.scraper

import echo.music.enhanced.kotlinytmusicscraper.YouTube
import echo.music.enhanced.kotlinytmusicscraper.YtMusicScraper
import echo.music.enhanced.kotlinytmusicscraper.models.MediaType
import echo.music.enhanced.kotlinytmusicscraper.models.SearchSuggestions
import echo.music.enhanced.kotlinytmusicscraper.models.response.PlayerResponse
import echo.music.enhanced.kotlinytmusicscraper.pages.SearchResult

/**
 * The "Innertube" scraper backend option (Settings > Scraper Backend). Only search and
 * stream-URL resolution — the two paths that actually break when YouTube changes something
 * server-side — are backed by the real vendored `:innertube` module (see `InnertubeBridge`).
 * Every other [YtMusicScraper] member delegates straight to [fallback] via Kotlin's `by`
 * clause, so nothing is ever unimplemented: this is a strict superset of [fallback]'s behavior,
 * not a second full scraper.
 *
 * Login (cookie/locale/proxy) state set through this interface only reaches [fallback] — the
 * vendored `:innertube` module has its own separate mutable client state, not synchronized here.
 * Search and playback stay fully functional signed-out; only login-personalized results are
 * affected while this backend is active.
 */
internal class InnertubeAdapter(
    private val fallback: YouTube,
) : YtMusicScraper by fallback {
    override suspend fun search(
        query: String,
        filter: YouTube.SearchFilter,
    ): Result<SearchResult> = innertubeSearch(query, filter)

    override suspend fun searchContinuation(continuation: String): Result<SearchResult> = innertubeSearchContinuation(continuation)

    override suspend fun getYTMusicSearchSuggestions(query: String): Result<SearchSuggestions> = innertubeSearchSuggestions(query)

    override suspend fun player(
        videoId: String,
        playlistId: String?,
        noLogIn: Boolean,
    ): Result<Triple<String?, PlayerResponse, MediaType>> = innertubePlayer(videoId, playlistId, noLogIn)
}
