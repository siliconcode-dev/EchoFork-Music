package echo.music.enhanced.spotify

import echo.music.enhanced.spotify.auth.SpotifyAuth
import echo.music.enhanced.spotify.model.response.spotify.CanvasResponse
import echo.music.enhanced.spotify.model.response.spotify.ClientTokenResponse
import echo.music.enhanced.spotify.model.response.spotify.PersonalTokenResponse
import echo.music.enhanced.spotify.model.response.spotify.SpotifyLyricsResponse
import echo.music.enhanced.spotify.model.response.spotify.search.SpotifySearchResponse
import io.ktor.client.call.body
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.http
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

class Spotify {
    private val spotifyClient = SpotifyClient()
    private val spotifyAuth = SpotifyAuth(spotifyClient)
    private val importJson = Json { isLenient = true; ignoreUnknownKeys = true }

    /**
     * Remove proxy for client
     */
    fun removeProxy() {
        spotifyClient.proxy = null
    }

    /**
     * Set the proxy for client
     */
    fun setProxy(
        isHttp: Boolean,
        host: String,
        port: Int,
    ) {
        val verifiedHost =
            if (!host.contains("http")) {
                "http://$host"
            } else {
                host
            }
        runCatching {
            if (isHttp) ProxyBuilder.http("$verifiedHost:$port") else ProxyBuilder.socks(verifiedHost, port)
        }.onSuccess {
            spotifyClient.proxy = it
        }.onFailure {
            it.printStackTrace()
        }
    }

    /**
     * Get personal token using the standard method
     */
    suspend fun getPersonalToken(spdc: String) =
        runCatching {
            spotifyClient.getSpotifyLyricsToken(spdc).body<PersonalTokenResponse>()
        }

    /**
     * Get personal token using the more reliable TOTP-based method
     * This should be used when the standard method fails
     */
    suspend fun getPersonalTokenWithTotp(spdc: String) = spotifyAuth.refreshToken(spdc)

    suspend fun getClientToken() =
        runCatching {
            spotifyClient
                .getSpotifyClientToken()
                .body<ClientTokenResponse>()
        }

    suspend fun searchSpotifyTrack(
        query: String,
        authToken: String,
        clientToken: String,
    ) = runCatching {
        spotifyClient
            .searchSpotifyTrack(query, authToken, clientToken)
            .body<SpotifySearchResponse>()
    }

    suspend fun getSpotifyLyrics(
        trackId: String,
        token: String,
        clientToken: String,
    ) = runCatching {
        spotifyClient
            .getSpotifyLyrics(
                token = token,
                clientToken = clientToken,
                trackId,
            ).body<SpotifyLyricsResponse>()
    }

    suspend fun getSpotifyCanvas(
        trackId: String,
        token: String,
        clientToken: String,
    ) = runCatching {
        spotifyClient.getSpotifyCanvas(trackId, token, clientToken).body<CanvasResponse>()
    }

    // ── Playlist import (v0.1.15): internal pathfinder v2 GraphQL, persisted-query hashes
    // sourced from https://github.com/sonic-liberation/hetu_spotify_gql_client (same registry
    // upstream Echo Music's own real Import-from-Spotify feature uses). Only the 3 operations
    // this fork's import flow needs are ported — library listing, one playlist's tracks, and
    // Liked Songs — not upstream's full Spotify.kt surface (search/home/mutations/etc).

    data class SpotifyImportPlaylist(
        val id: String,
        val name: String,
        val trackCount: Int?,
        val thumbnailUrl: String?,
    )

    data class SpotifyImportTrack(
        val title: String,
        val artist: String,
        val durationMs: Int,
    )

    data class SpotifyImportPage<T>(
        val items: List<T>,
        val total: Int,
    )

    private object ImportHashes {
        const val LIBRARY_V3 = "973e511ca44261fda7eebac8b653155e7caee3675abb4fb110cc1b8c78b091c3"
        const val FETCH_PLAYLIST = "346811f856fb0b7e4f6c59f8ebea78dd081c6e2fb01b77c954b26259d5fc6763"
        const val FETCH_LIBRARY_TRACKS = "087278b20b743578a6262c2b0b4bcd20d879c503cc359a2285baf083ef944240"
    }

    private fun JsonObject.obj(key: String): JsonObject? = this[key]?.takeIf { it !is JsonNull }?.jsonObject

    private fun JsonObject.str(key: String): String? = this[key]?.takeIf { it !is JsonNull }?.jsonPrimitive?.contentOrNull

    private fun JsonObject.int(key: String): Int? = this[key]?.takeIf { it !is JsonNull }?.jsonPrimitive?.intOrNull

    private fun JsonObject.arr(key: String): JsonArray? = this[key]?.takeIf { it !is JsonNull }?.jsonArray

    private fun parseImportTrack(trackData: JsonObject): SpotifyImportTrack? {
        val name = trackData.str("name") ?: return null
        val artist =
            trackData.obj("artists")?.arr("items")?.firstOrNull()?.jsonObject?.obj("profile")?.str("name").orEmpty()
        val durationMs =
            trackData.obj("duration")?.int("totalMilliseconds")
                ?: trackData.int("durationMs")
                ?: 0
        return SpotifyImportTrack(title = name, artist = artist, durationMs = durationMs)
    }

    /** Lists the user's own + followed playlists (flattened, folders ignored — this fork has no folder concept). */
    suspend fun getLibraryPlaylists(
        accessToken: String,
        limit: Int = 50,
        offset: Int = 0,
    ): Result<SpotifyImportPage<SpotifyImportPlaylist>> =
        runCatching {
            val variables =
                buildJsonObject {
                    putJsonArray("filters") { add("Playlists") }
                    put("order", null as String?)
                    put("textFilter", "")
                    putJsonArray("features") {
                        add("LIKED_SONGS")
                        add("YOUR_EPISODES_V2")
                        add("PRERELEASES")
                        add("EVENTS")
                    }
                    put("limit", limit)
                    put("offset", offset)
                    put("flatten", true)
                    putJsonArray("expandedFolders") {}
                    put("folderUri", null as String?)
                    put("includeFoldersWhenFlattening", false)
                }
            val response =
                spotifyClient
                    .graphqlQueryV2("libraryV3", ImportHashes.LIBRARY_V3, variables, accessToken)
                    .body<String>()
            val root = importJson.parseToJsonElement(response).jsonObject
            val libraryData =
                root.obj("data")?.obj("me")?.obj("libraryV3")
                    ?: throw IllegalStateException("Invalid libraryV3 response")
            val playlists =
                libraryData.arr("items")?.mapNotNull { itemElem ->
                    val wrapper = itemElem.jsonObject.obj("item") ?: return@mapNotNull null
                    if (wrapper.str("__typename") != "PlaylistResponseWrapper") return@mapNotNull null
                    val data = wrapper.obj("data") ?: return@mapNotNull null
                    val uri = wrapper.str("_uri") ?: return@mapNotNull null
                    SpotifyImportPlaylist(
                        id = uri.substringAfterLast(":"),
                        name = data.str("name").orEmpty(),
                        trackCount =
                            data.obj("content")?.int("totalCount")
                                ?: data.obj("contents")?.int("totalCount"),
                        thumbnailUrl =
                            data.obj("images")?.arr("items")?.firstOrNull()?.jsonObject
                                ?.arr("sources")?.firstOrNull()?.jsonObject?.str("url"),
                    )
                } ?: emptyList()
            SpotifyImportPage(items = playlists, total = libraryData.int("totalCount") ?: playlists.size)
        }

    /** Paginates one playlist's tracks. */
    suspend fun getPlaylistTracks(
        accessToken: String,
        playlistId: String,
        limit: Int = 100,
        offset: Int = 0,
    ): Result<SpotifyImportPage<SpotifyImportTrack>> =
        runCatching {
            val variables =
                buildJsonObject {
                    put("uri", "spotify:playlist:$playlistId")
                    put("offset", offset)
                    put("limit", limit)
                    put("enableWatchFeedEntrypoint", false)
                }
            val response =
                spotifyClient
                    .graphqlQueryV2("fetchPlaylist", ImportHashes.FETCH_PLAYLIST, variables, accessToken)
                    .body<String>()
            val root = importJson.parseToJsonElement(response).jsonObject
            val content =
                root.obj("data")?.obj("playlistV2")?.obj("content")
                    ?: throw IllegalStateException("Invalid fetchPlaylist response")
            val tracks =
                content.arr("items")?.mapNotNull { elem ->
                    val itemData = elem.jsonObject.obj("itemV2")?.obj("data") ?: return@mapNotNull null
                    parseImportTrack(itemData)
                } ?: emptyList()
            SpotifyImportPage(items = tracks, total = content.int("totalCount") ?: tracks.size)
        }

    /** Paginates the user's Liked Songs. */
    suspend fun getLikedSongs(
        accessToken: String,
        limit: Int = 50,
        offset: Int = 0,
    ): Result<SpotifyImportPage<SpotifyImportTrack>> =
        runCatching {
            val variables = buildJsonObject { put("offset", offset); put("limit", limit) }
            val response =
                spotifyClient
                    .graphqlQueryV2("fetchLibraryTracks", ImportHashes.FETCH_LIBRARY_TRACKS, variables, accessToken)
                    .body<String>()
            val root = importJson.parseToJsonElement(response).jsonObject
            val tracksData =
                root.obj("data")?.obj("me")?.obj("library")?.obj("tracks")
                    ?: throw IllegalStateException("Invalid fetchLibraryTracks response")
            val tracks =
                tracksData.arr("items")?.mapNotNull { elem ->
                    val trackData = elem.jsonObject.obj("track")?.obj("data") ?: return@mapNotNull null
                    parseImportTrack(trackData)
                } ?: emptyList()
            SpotifyImportPage(items = tracks, total = tracksData.int("totalCount") ?: tracks.size)
        }
}