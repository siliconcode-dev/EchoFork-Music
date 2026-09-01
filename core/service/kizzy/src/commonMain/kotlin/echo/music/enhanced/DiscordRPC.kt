package echo.music.enhanced

import echo.music.enhanced.domain.data.entities.SongEntity
import echo.music.enhanced.rpc.KizzyRPC
import echo.music.enhanced.rpc.RpcImage
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class DiscordRPC(
    token: String,
) : KizzyRPC(token) {
    @OptIn(ExperimentalTime::class)
    suspend fun updateSong(
        currentPlaybackTimeMillis: Long,
        durationMillis: Long,
        playbackSpeed: Float = 1.0f,
        song: SongEntity,
    ) = runCatching {
        // No Discord application registered for this build — nothing to publish to.
        if (!isConfigured) return@runCatching

        val currentTime = Clock.System.now().toEpochMilliseconds()

        val adjustedPlaybackTime = (currentPlaybackTimeMillis / playbackSpeed).toLong()
        val calculatedStartTime = currentTime - adjustedPlaybackTime

        val remainingDuration = durationMillis - currentPlaybackTimeMillis
        val adjustedRemainingDuration = (remainingDuration / playbackSpeed).toLong()

        setActivity(
            name = APP_NAME,
            details = song.title,
            state = song.artistName?.joinToString(", "),
            largeImage = song.thumbnails?.let { RpcImage.ExternalImage(it) },
            smallImage = RpcImage.ExternalImage(APP_ICON),
            largeText = song.albumName,
            smallText = song.artistName?.firstOrNull(),
            buttons =
                listOf(
                    "Listen on YouTube Music" to "https://music.youtube.com/watch?v=${song.videoId}",
                    "Get Enhanced Echo Music" to "https://github.com/siliconcode-dev/EchoFork-Music",
                ),
            type = Type.LISTENING,
            since = currentTime,
            startTime = calculatedStartTime,
            endTime = currentTime + adjustedRemainingDuration,
            applicationId = APPLICATION_ID,
        )
    }

    companion object {
        // TODO(branding): register a Discord application for Enhanced Echo Music at
        //  https://discord.com/developers/applications and paste its ID here.
        //  The previous value ("1271273225120125040") was SimpMusic's application,
        //  which this project does not own. Rich Presence stays disabled while this
        //  is blank — see isConfigured below.
        private const val APPLICATION_ID = ""

        // TODO(branding): shown as the Discord app name. Only takes effect once
        //  APPLICATION_ID above is set; Discord displays the registered app's own
        //  name for the presence header.
        private const val APP_NAME: String = "Enhanced Echo Music"

        // Self-hosted via this repo's raw content instead of depending on SimpMusic's
        // asset storage. Requires the `main` branch to exist with this file on it.
        private const val APP_ICON: String =
            "https://raw.githubusercontent.com/siliconcode-dev/EchoFork-Music/main/assets/Echo-new.png"

        /**
         * Whether a Discord application has been configured for this build.
         *
         * Without an application ID Discord rejects the presence payload, so we skip
         * the call entirely rather than issue requests that cannot succeed.
         */
        val isConfigured: Boolean get() = APPLICATION_ID.isNotBlank()
    }
}