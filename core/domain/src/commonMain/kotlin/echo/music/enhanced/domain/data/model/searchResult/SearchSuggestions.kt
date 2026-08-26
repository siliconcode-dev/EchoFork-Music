package echo.music.enhanced.domain.data.model.searchResult

import echo.music.enhanced.domain.data.type.SearchResultType

data class SearchSuggestions(
    val queries: List<String>,
    val recommendedItems: List<SearchResultType>,
)