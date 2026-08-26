package echo.music.enhanced.domain.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import echo.music.enhanced.domain.data.model.browse.album.Track

@Entity(tableName = "queue")
data class QueueEntity(
    @PrimaryKey(autoGenerate = false)
    val queueId: Long = 0,
    val listTrack: List<Track>,
)