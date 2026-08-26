package echo.music.enhanced.data.parser

import echo.music.enhanced.domain.data.model.searchResult.playlists.PlaylistsResult
import echo.music.enhanced.kotlinytmusicscraper.models.GridRenderer
import echo.music.enhanced.kotlinytmusicscraper.models.MusicTwoRowItemRenderer

internal fun parseLibraryPlaylist(input: List<GridRenderer.Item>): List<PlaylistsResult> {
    val list: MutableList<PlaylistsResult> = mutableListOf()
    if (input.isNotEmpty()) {
        for (i in input.indices) {
            input[i].musicTwoRowItemRenderer?.let {
                if (it.navigationEndpoint?.browseEndpoint?.browseId != null) {
                    list.add(
                        PlaylistsResult(
                            author =
                                it.subtitle
                                    ?.runs
                                    ?.get(0)
                                    ?.text ?: "",
                            browseId = it.navigationEndpoint?.browseEndpoint?.browseId ?: "",
                            category = "",
                            itemCount = "",
                            resultType = "",
                            thumbnails =
                                it.thumbnailRenderer
                                    ?.musicThumbnailRenderer
                                    ?.thumbnail
                                    ?.thumbnails
                                    ?.toListThumbnail() ?: listOf(),
                            title =
                                it.title
                                    ?.runs
                                    ?.get(0)
                                    ?.text ?: "",
                        ),
                    )
                }
            }
        }
    }
    return list
}

internal fun parseNextLibraryPlaylist(input: List<MusicTwoRowItemRenderer>): List<PlaylistsResult> =
    input.map {
        PlaylistsResult(
            author =
                it.subtitle
                    ?.runs
                    ?.get(0)
                    ?.text ?: "",
            browseId = it.navigationEndpoint?.browseEndpoint?.browseId ?: "",
            category = "",
            itemCount = "",
            resultType = "",
            thumbnails =
                it.thumbnailRenderer
                    ?.musicThumbnailRenderer
                    ?.thumbnail
                    ?.thumbnails
                    ?.toListThumbnail() ?: listOf(),
            title =
                it.title
                    ?.runs
                    ?.get(0)
                    ?.text ?: "",
        )
    }