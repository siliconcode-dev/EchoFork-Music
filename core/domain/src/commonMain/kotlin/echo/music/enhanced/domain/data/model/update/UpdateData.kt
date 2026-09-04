package echo.music.enhanced.domain.data.model.update

data class UpdateData(
    val tagName: String,
    val releaseTime: String?,
    val body: String,
    val assets: List<UpdateAsset> = emptyList(),
)

data class UpdateAsset(
    val name: String,
    val downloadUrl: String,
    val size: Long,
)
