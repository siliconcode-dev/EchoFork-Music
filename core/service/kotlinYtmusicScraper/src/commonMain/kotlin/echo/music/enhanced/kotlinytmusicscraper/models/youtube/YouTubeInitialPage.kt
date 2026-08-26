package echo.music.enhanced.kotlinytmusicscraper.models.youtube

import echo.music.enhanced.kotlinytmusicscraper.models.ResponseContext
import echo.music.enhanced.kotlinytmusicscraper.models.response.PlayerResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YouTubeInitialPage(
    @SerialName("videoDetails")
    val videoDetails: VideoDetails?,
    @SerialName("captions")
    val captions: Captions? = null,
) {
    @Serializable
    data class Captions(
        @SerialName("playerCaptionsTracklistRenderer")
        val playerCaptionsTracklistRenderer: PlayerCaptionsTracklistRenderer?,
    ) {
        @Serializable
        data class PlayerCaptionsTracklistRenderer(
            @SerialName("captionTracks")
            val captionTracks: List<CaptionTrack>?,
        ) {
            @Serializable
            data class CaptionTrack(
                @SerialName("baseUrl")
                val baseUrl: String?,
                @SerialName("name")
                val name: Name?,
                @SerialName("vssId")
                val vssId: String?,
            ) {
                @Serializable
                data class Name(
                    @SerialName("simpleText")
                    val simpleText: String?,
                )
            }
        }
    }
}

@Serializable
data class GhostResponse(
    val responseContext: ResponseContext,
    val playbackTracking: PlayerResponse.PlaybackTracking? = null
)