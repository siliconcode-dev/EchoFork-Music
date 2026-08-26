package echo.music.enhanced.domain.data.model.home

import echo.music.enhanced.domain.data.model.home.chart.Chart
import echo.music.enhanced.domain.data.model.mood.Mood
import echo.music.enhanced.domain.utils.Resource

data class HomeResponse(
    val homeItem: Resource<ArrayList<HomeItem>>,
    val exploreMood: Resource<Mood>,
    val exploreChart: Resource<Chart>,
)