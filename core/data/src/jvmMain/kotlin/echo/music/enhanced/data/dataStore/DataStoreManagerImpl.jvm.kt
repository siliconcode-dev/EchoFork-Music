package echo.music.enhanced.data.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import echo.music.enhanced.common.SETTINGS_FILENAME
import echo.music.enhanced.data.io.getHomeFolderPath
import createDataStore
import java.io.File

actual fun createDataStoreInstance(): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(getHomeFolderPath(listOf(".enhanced-echo-music")), "$SETTINGS_FILENAME.preferences_pb")
        file.absolutePath
    }
)