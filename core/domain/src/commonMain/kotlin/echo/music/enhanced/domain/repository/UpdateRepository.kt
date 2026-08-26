package echo.music.enhanced.domain.repository

import echo.music.enhanced.domain.data.model.update.UpdateData
import echo.music.enhanced.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UpdateRepository {
    fun checkForGithubReleaseUpdate(): Flow<Resource<UpdateData>>
    fun checkForFdroidUpdate(): Flow<Resource<UpdateData>>
}