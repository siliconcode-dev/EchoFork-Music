package echo.music.enhanced.domain.data.model.home

import echo.music.enhanced.domain.data.model.home.chart.Chart
import echo.music.enhanced.domain.data.model.mood.Mood
import echo.music.enhanced.domain.utils.Resource

data class HomeDataCombine(
    val home: Resource<Pair<String?, List<HomeItem>>>,
    val mood: Resource<Mood>,
    val chart: Resource<Chart>,
    val newRelease: Resource<List<HomeItem>>,
)