package echo.music.enhanced.paxsenixlyrics

import android.content.Context
import echo.music.enhanced.betterlyrics.TTMLParser
import echo.music.enhanced.logger.Logger
import echo.music.enhanced.paxsenixlyrics.models.LyricsResponse
import echo.music.enhanced.paxsenixlyrics.models.SearchResponse
import echo.music.enhanced.paxsenixlyrics.models.SearchResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.selects.select
import kotlinx.serialization.json.Json
import java.util.Locale
import kotlin.math.abs

object Paxsenix {
    private const val TAG = "Paxsenix"

    @Volatile
    private var client: HttpClient? = null
    private var appVersion: String = "Unknown"

    fun init(context: Context) {
        if (client != null) return

        synchronized(this) {
            if (client != null) return

            appVersion =
                try {
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
                        ?: "Unknown"
                } catch (e: Exception) {
                    Logger.e(TAG, "Failed to get app version", e)
                    "Unknown"
                }

            Logger.d(TAG, "Initializing Paxsenix with version: $appVersion")

            client =
                HttpClient(CIO) {
                    install(HttpTimeout) {
                        requestTimeoutMillis = 5_000
                        connectTimeoutMillis = 4_000
                    }
                    install(ContentNegotiation) {
                        json(
                            Json {
                                isLenient = true
                                ignoreUnknownKeys = true
                            },
                        )
                    }

                    defaultRequest {
                        url("https://lyrics.paxsenix.org")
                        header("User-Agent", "echomusic/$appVersion")
                    }

                    expectSuccess = true
                }

            Logger.d(TAG, "Paxsenix HTTP client initialized")
        }
    }

    private val httpClient: HttpClient
        get() = client ?: throw IllegalStateException("Paxsenix.init() must be called before using Paxsenix")

    private val titleCleanupPatterns =
        listOf(
            Regex(
                """\s*\(.*?(official|video|audio|lyrics|lyric|visualizer|hd|hq|4k|remaster|remix|live|acoustic|version|edit|extended|radio|clean|explicit).*?\)""",
                RegexOption.IGNORE_CASE,
            ),
            Regex(
                """\s*\[.*?(official|video|audio|lyrics|lyric|visualizer|hd|hq|4k|remaster|remix|live|acoustic|version|edit|extended|radio|clean|explicit).*?\]""",
                RegexOption.IGNORE_CASE,
            ),
            Regex("""\s*【.*?】"""),
            Regex("""\s*\|.*$"""),
            Regex("""\s*-\s*(official|video|audio|lyrics|lyric|visualizer).*$""", RegexOption.IGNORE_CASE),
            Regex("""\s*\(feat\..*?\)""", RegexOption.IGNORE_CASE),
            Regex("""\s*\(ft\..*?\)""", RegexOption.IGNORE_CASE),
            Regex("""\s*feat\..*$""", RegexOption.IGNORE_CASE),
            Regex("""\s*ft\..*$""", RegexOption.IGNORE_CASE),
            Regex("""\s*\([^)]*\d{4}[^)]*\)""", RegexOption.IGNORE_CASE),
        )

    private val artistSeparators =
        listOf(" & ", " and ", ", ", " x ", " X ", " feat. ", " feat ", " ft. ", " ft ", " featuring ", " with ")

    private fun cleanTitle(title: String): String {
        var cleaned = title.trim()
        for (pattern in titleCleanupPatterns) {
            cleaned = cleaned.replace(pattern, "")
        }
        return cleaned.trim()
    }

    private fun cleanArtist(artist: String): String {
        var cleaned = artist.trim()
        for (separator in artistSeparators) {
            if (cleaned.contains(separator, ignoreCase = true)) {
                cleaned = cleaned.split(separator, ignoreCase = true, limit = 2)[0]
                break
            }
        }
        return cleaned.trim()
    }

    private suspend fun search(query: String): List<SearchResult> =
        runCatching {
            Logger.d(TAG, "Searching for: $query")
            val response =
                httpClient.get("/apple-music/search") {
                    parameter("q", query)
                }.body<SearchResponse>()

            Logger.d(TAG, "Search results count: ${response.size}")
            response
        }.getOrElse { e ->
            Logger.e(TAG, "Search error: ${e.message}", e)
            emptyList()
        }

    suspend fun getLyrics(
        title: String,
        artist: String,
        duration: Int,
        album: String? = null,
    ): Result<String> =
        runCatching {
            val cleanedTitle = cleanTitle(title)
            val cleanedArtist = cleanArtist(artist)

            Logger.d(TAG, "getLyrics: title='$title', artist='$artist', duration=$duration")

            val searchQueries =
                buildList {
                    add("$cleanedTitle $cleanedArtist")
                    add(cleanedTitle)
                    if (!album.isNullOrBlank()) {
                        add("$cleanedTitle $cleanedArtist $album")
                    }
                }

            var allResults: List<Pair<SearchResult, Double>> = emptyList()

            for (query in searchQueries) {
                if (allResults.isEmpty()) {
                    val searchResults = search(query)
                    if (searchResults.isNotEmpty()) {
                        allResults = scoreAndFilterResults(searchResults, title, artist, duration)
                    }
                }
            }

            if (allResults.isEmpty()) {
                Logger.w(TAG, "No tracks found for any query")
                throw IllegalStateException("No tracks found on Paxsenix")
            }

            // Fetch top candidates in parallel; return the first word-timed result,
            // or the best plain/line-synced result if none have word timings.
            val candidates = allResults.take(5)
            val scope = CoroutineScope(Dispatchers.IO)
            val jobs =
                candidates.map { (result, score) ->
                    scope.async {
                        Logger.d(TAG, "Fetching (parallel): ${result.displayName} (ID: ${result.id}, score: $score)")
                        result to runCatching { fetchLyricsForTrackWithType(result.id) }.getOrDefault("" to false)
                    }
                }

            var plainFallback: String? = null
            try {
                val remaining = jobs.toMutableList()
                while (remaining.isNotEmpty()) {
                    val (_, lrcPair) =
                        select {
                            remaining.forEach { deferred -> deferred.onAwait { it } }
                        }
                    remaining.removeAll { it.isCompleted }
                    val (lrc, hasWordTimings) = lrcPair
                    if (lrc.isNotEmpty()) {
                        if (hasWordTimings) {
                            return@runCatching lrc
                        } else if (plainFallback == null) {
                            plainFallback = lrc
                        }
                    }
                }
            } finally {
                scope.coroutineContext.cancelChildren()
            }

            plainFallback?.let {
                Logger.d(TAG, "Using Paxsenix lyrics without word-level sync")
                return@runCatching it
            }
            Logger.w(TAG, "No lyrics content from Paxsenix for matched tracks")
            throw IllegalStateException("No lyrics available from Paxsenix")
        }

    private fun scoreAndFilterResults(
        results: List<SearchResult>,
        title: String,
        artist: String,
        duration: Int,
    ): List<Pair<SearchResult, Double>> {
        val durationMs = duration * 1000
        val cleanupRegex = Regex("""\s*\(.*?\)|\s*\[.*?\]""")

        val cleanedTitle = title.replace(cleanupRegex, "").lowercase().trim()
        val cleanedArtist = cleanArtist(artist).lowercase()

        val targetIsMixed = title.contains("mixed", ignoreCase = true)
        val targetIsRemix = title.contains("remix", ignoreCase = true)

        return results.map { result ->
            var score = 0.0

            result.duration?.let { d ->
                val diff = abs(d - durationMs)
                when {
                    diff <= 2000 -> score += 100
                    diff <= 5000 -> score += 50
                    diff <= 10000 -> score += 10
                    else -> score -= 50
                }
            }

            val resultTitleCleaned = result.displayName.replace(cleanupRegex, "").lowercase().trim()
            when {
                resultTitleCleaned == cleanedTitle -> score += 80
                resultTitleCleaned.contains(cleanedTitle) || cleanedTitle.contains(resultTitleCleaned) -> score += 40
            }

            val resultIsMixed = result.displayName.contains("mixed", ignoreCase = true)
            val resultIsRemix = result.displayName.contains("remix", ignoreCase = true)
            if (resultIsMixed && !targetIsMixed) score -= 60
            if (resultIsRemix && !targetIsRemix) score -= 40

            val resultArtistLower = result.displayArtist.lowercase()
            when {
                resultArtistLower.contains(cleanedArtist) -> score += 50
                else -> {
                    val artistWords = cleanedArtist.split(Regex("\\s+")).filter { it.length > 2 }
                    if (artistWords.any { resultArtistLower.contains(it) }) score += 25
                }
            }

            result to score
        }.sortedByDescending { it.second }.filter { it.second > 0 }.take(10)
    }

    private suspend fun fetchLyricsForTrackWithType(id: String): Pair<String, Boolean> {
        val result = fetchLyricsForTrack(id)
        if (result.isSuccess) {
            val lrc = result.getOrNull()!!
            val hasWordTimings = lrc.contains("<") && lrc.contains(">")
            return lrc to hasWordTimings
        }
        return "" to false
    }

    private suspend fun fetchLyricsForTrack(id: String): Result<String> =
        runCatching {
            Logger.d(TAG, "Fetching lyrics for track ID: $id")

            val response =
                httpClient.get("/apple-music/lyrics") {
                    parameter("id", id)
                }.body<LyricsResponse>()

            val lyricsType = response.type
            Logger.d(TAG, "Lyrics response: type=$lyricsType")

            if (!response.ttmlContent.isNullOrBlank()) {
                val lrc = convertTTMLToAppFormat(response.ttmlContent)
                if (lrc.isNotEmpty()) {
                    Logger.d(TAG, "Generated LRC from ttmlContent using TTMLParser")
                    return@runCatching lrc
                }
            }

            if (!response.elrcMultiPerson.isNullOrBlank()) {
                Logger.d(TAG, "Using elrcMultiPerson as fallback")
                return@runCatching response.elrcMultiPerson
            }
            if (!response.elrc.isNullOrBlank()) {
                Logger.d(TAG, "Using elrc as fallback")
                return@runCatching response.elrc
            }
            if (!response.plain.isNullOrBlank()) {
                Logger.d(TAG, "Using plain lyrics field")
                return@runCatching response.plain
            }

            if (response.content.isEmpty()) {
                throw IllegalStateException("No lyrics found")
            }

            val hasWordLevel = lyricsType == "Syllable"
            Logger.d(TAG, "Using content array as source, hasWordLevel=$hasWordLevel")

            if (!hasWordLevel) {
                val plain =
                    response.content
                        .map { line -> line.text.joinToString(" ") { it.text } }
                        .filter { it.isNotBlank() }
                        .joinToString("\n")
                return@runCatching plain
            }

            val lrc =
                buildString {
                    response.content.forEach { line ->
                        val timeMs = line.timestamp
                        val minutes = timeMs / 1000 / 60
                        val seconds = (timeMs / 1000) % 60
                        val centiseconds = (timeMs % 1000) / 10

                        val agent =
                            when {
                                line.background -> "{bg}"
                                line.oppositeTurn -> "{agent:v2}"
                                else -> "{agent:v1}"
                            }

                        val lineText = line.text.joinToString(" ") { it.text }

                        if (lineText.isNotBlank()) {
                            appendLine(
                                String.format(
                                    Locale.US,
                                    "[%02d:%02d.%02d]%s%s",
                                    minutes,
                                    seconds,
                                    centiseconds,
                                    agent,
                                    lineText,
                                ),
                            )
                            if (line.text.isNotEmpty()) {
                                val wordsData =
                                    line.text.joinToString("|") { word ->
                                        "${word.text}:${word.timestamp.toDouble() / 1000}:${word.endtime.toDouble() / 1000}"
                                    }
                                if (wordsData.isNotEmpty()) appendLine("<$wordsData>")
                            }
                        }
                    }
                }

            return@runCatching lrc
        }

    suspend fun getAllLyrics(
        title: String,
        artist: String,
        duration: Int,
        album: String? = null,
        callback: (String) -> Unit,
    ) {
        val cleanedTitle = cleanTitle(title)
        val cleanedArtist = cleanArtist(artist)

        val searchQueries = listOf("$cleanedTitle $cleanedArtist", cleanedTitle)
        var plainFallback: String? = null
        var scoredResults: List<Pair<SearchResult, Double>> = emptyList()

        for (query in searchQueries) {
            val results = search(query)
            if (results.isEmpty()) continue
            val filtered = scoreAndFilterResults(results, title, artist, duration)
            if (filtered.isNotEmpty()) {
                scoredResults = filtered
                break
            }
        }

        // Fetch top 3 candidates in parallel; deliver word-timed result first.
        val candidates = scoredResults.take(3)
        val scope = CoroutineScope(Dispatchers.IO)
        val jobs =
            candidates.map { (result, _) ->
                scope.async {
                    Logger.d(TAG, "Fetching (parallel/all): ${result.displayName}")
                    runCatching { fetchLyricsForTrackWithType(result.id) }.getOrDefault("" to false)
                }
            }
        try {
            val remaining = jobs.toMutableList()
            while (remaining.isNotEmpty()) {
                val (lrc, hasWordTimings) =
                    select {
                        remaining.forEach { deferred -> deferred.onAwait { it } }
                    }
                remaining.removeAll { it.isCompleted }
                if (lrc.isNotEmpty()) {
                    if (hasWordTimings) {
                        callback(lrc)
                        return
                    } else if (plainFallback == null) {
                        plainFallback = lrc
                    }
                }
            }
        } finally {
            scope.coroutineContext.cancelChildren()
        }

        plainFallback?.let {
            Logger.d(TAG, "Offering plain/non-synced lyrics as fallback")
            callback(it)
        }
    }

    private fun convertTTMLToAppFormat(ttml: String): String =
        try {
            val parsedLines = TTMLParser.parseTTML(ttml)
            TTMLParser.toLRC(parsedLines)
        } catch (e: Exception) {
            Logger.e(TAG, "TTML conversion failed: ${e.message}", e)
            ""
        }
}
