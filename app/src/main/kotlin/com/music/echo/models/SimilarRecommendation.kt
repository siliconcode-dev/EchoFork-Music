

package echo.music.enhanced.models

import com.music.innertube.models.YTItem
import echo.music.enhanced.db.entities.LocalItem

data class SimilarRecommendation(
    val title: LocalItem,
    val items: List<YTItem>,
)
