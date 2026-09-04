package echo.music.enhanced.domain.repository

import echo.music.enhanced.domain.data.model.update.UpdateData
import echo.music.enhanced.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UpdateRepository {
    fun checkForGithubReleaseUpdate(): Flow<Resource<UpdateData>>
    fun checkForFdroidUpdate(): Flow<Resource<UpdateData>>

    /**
     * Streams (isDone, progressFraction 0..1, speedKbps) while downloading [downloadUrl] to
     * [destinationPath] — isDone with progress < 1f means the download failed.
     */
    fun downloadApk(
        downloadUrl: String,
        destinationPath: String,
    ): Flow<Triple<Boolean, Float, Int>>
}