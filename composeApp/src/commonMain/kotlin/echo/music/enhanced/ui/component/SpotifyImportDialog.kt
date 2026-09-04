package echo.music.enhanced.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import echo.music.enhanced.domain.repository.SpotifyImportState
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.import_spotify_playlist
import echomusic.composeapp.generated.resources.ok
import echomusic.composeapp.generated.resources.spotify_import_preparing
import echomusic.composeapp.generated.resources.spotify_import_source_progress
import echomusic.composeapp.generated.resources.spotify_import_summary
import echomusic.composeapp.generated.resources.spotify_import_track_progress
import echomusic.composeapp.generated.resources.spotify_import_unresolved
import org.jetbrains.compose.resources.stringResource

/**
 * Foreground progress + summary dialog for "Import from Spotify" (v0.1.15) — same visual language
 * as [CreateAiPlaylistDialog]. [state] is null only for the brief window between dispatch and the
 * import flow's first emission, shown as a generic "connecting" spinner.
 */
@Composable
fun SpotifyImportDialog(
    state: SpotifyImportState?,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { if (state !is SpotifyImportState.Progress) onDismiss() },
        title = { Text(stringResource(Res.string.import_spotify_playlist)) },
        text = {
            when (state) {
                null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text(stringResource(Res.string.spotify_import_preparing))
                    }
                }

                is SpotifyImportState.Progress -> {
                    val progress = state.progress
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text(
                            stringResource(
                                Res.string.spotify_import_source_progress,
                                progress.sourceTitle,
                                progress.completedSources + 1,
                                progress.totalSources,
                            ),
                        )
                        Spacer(Modifier.height(8.dp))
                        val fraction =
                            if (progress.totalTracks > 0) progress.matchedTracks.toFloat() / progress.totalTracks else 0f
                        LinearProgressIndicator(progress = { fraction }, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(8.dp))
                        Text(stringResource(Res.string.spotify_import_track_progress, progress.matchedTracks, progress.totalTracks))
                    }
                }

                is SpotifyImportState.Done -> {
                    val summary = state.summary
                    Column {
                        Text(
                            stringResource(
                                Res.string.spotify_import_summary,
                                summary.importedTracks,
                                summary.totalTracks,
                                summary.sources.size,
                            ),
                        )
                        if (summary.failedTracks > 0) {
                            Spacer(Modifier.height(8.dp))
                            Text(stringResource(Res.string.spotify_import_unresolved, summary.failedTracks))
                        }
                    }
                }

                is SpotifyImportState.Failed -> {
                    Text(state.message)
                }
            }
        },
        confirmButton = {
            if (state is SpotifyImportState.Done || state is SpotifyImportState.Failed) {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.ok))
                }
            }
        },
    )
}
