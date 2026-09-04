package echo.music.enhanced.domain.manager

import echo.music.enhanced.domain.data.model.update.UpdateDownloadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Shared in-memory bridge between the Android update-download service (which runs the actual
 * foreground download, outside any ViewModel's lifecycle) and the UI's live progress display —
 * a Koin singleton rather than DataStore-backed, since this state is inherently transient and
 * resetting on process death is fine for a progress indicator.
 */
class UpdateDownloadManager {
    private val _state = MutableStateFlow<UpdateDownloadState>(UpdateDownloadState.Idle)
    val state: StateFlow<UpdateDownloadState> = _state.asStateFlow()

    fun setState(newState: UpdateDownloadState) {
        _state.value = newState
    }
}
