package echo.music.enhanced.data.parser.search

import echo.music.enhanced.domain.data.model.searchResult.artists.ArtistsResult
import echo.music.enhanced.domain.data.model.searchResult.songs.Thumbnail
import echo.music.enhanced.kotlinytmusicscraper.models.ArtistItem
import echo.music.enhanced.kotlinytmusicscraper.pages.SearchResult

internal fun parseSearchArtist(result: SearchResult): ArrayList<ArtistsResult> {
    val artistsResult: ArrayList<ArtistsResult> = arrayListOf()
    result.items.forEach {
        val artist = it as ArtistItem
        artistsResult.add(
            ArtistsResult(
                artist = artist.title,
                browseId = artist.id,
                category = "Artist",
                radioId = artist.radioEndpoint?.playlistId ?: "",
                resultType = "Artist",
                shuffleId = artist.shuffleEndpoint?.playlistId ?: "",
                thumbnails = listOf(Thumbnail(544, Regex("([wh])120").replace(artist.thumbnail, "$1544"), 544)),
            ),
        )
    }
    return artistsResult
}