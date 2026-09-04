package echo.music.enhanced.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import echo.music.enhanced.domain.utils.LocalResource
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.ai_playlist_generate
import echomusic.composeapp.generated.resources.ai_playlist_generating
import echomusic.composeapp.generated.resources.ai_playlist_generation_failed
import echomusic.composeapp.generated.resources.ai_playlist_modification_failed
import echomusic.composeapp.generated.resources.ai_playlist_modify_hint
import echomusic.composeapp.generated.resources.ai_playlist_modifying
import echomusic.composeapp.generated.resources.ai_playlist_prompt_hint
import echomusic.composeapp.generated.resources.ai_playlist_song_count
import echomusic.composeapp.generated.resources.ai_playlist_try_again
import echomusic.composeapp.generated.resources.cancel
import echomusic.composeapp.generated.resources.create_playlist_with_ai
import echomusic.composeapp.generated.resources.modify_playlist_with_ai
import org.jetbrains.compose.resources.stringResource

@Composable
fun CreateAiPlaylistDialog(
    state: LocalResource<Long>?,
    onGenerate: (prompt: String, songCount: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var prompt by remember { mutableStateOf("") }
    var songCount by remember { mutableFloatStateOf(15f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.create_playlist_with_ai)) },
        text = {
            when (state) {
                is LocalResource.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text(stringResource(Res.string.ai_playlist_generating))
                    }
                }

                is LocalResource.Error -> {
                    Text(stringResource(Res.string.ai_playlist_generation_failed))
                }

                else -> {
                    Column {
                        OutlinedTextField(
                            value = prompt,
                            onValueChange = { prompt = it },
                            label = { Text(stringResource(Res.string.ai_playlist_prompt_hint)) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(stringResource(Res.string.ai_playlist_song_count, songCount.toInt()))
                        Slider(
                            value = songCount,
                            onValueChange = { songCount = it },
                            valueRange = 5f..30f,
                            steps = 24,
                        )
                    }
                }
            }
        },
        confirmButton = {
            when (state) {
                is LocalResource.Loading -> {}
                is LocalResource.Error -> {
                    TextButton(onClick = { onGenerate(prompt, songCount.toInt()) }) {
                        Text(stringResource(Res.string.ai_playlist_try_again))
                    }
                }

                else -> {
                    TextButton(
                        onClick = { onGenerate(prompt, songCount.toInt()) },
                        enabled = prompt.isNotBlank(),
                    ) {
                        Text(stringResource(Res.string.ai_playlist_generate))
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        },
    )
}

@Composable
fun ModifyPlaylistWithAiDialog(
    state: LocalResource<String>?,
    onModify: (prompt: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var prompt by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.modify_playlist_with_ai)) },
        text = {
            when (state) {
                is LocalResource.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text(stringResource(Res.string.ai_playlist_modifying))
                    }
                }

                is LocalResource.Error -> {
                    Text(stringResource(Res.string.ai_playlist_modification_failed))
                }

                else -> {
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text(stringResource(Res.string.ai_playlist_modify_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        },
        confirmButton = {
            when (state) {
                is LocalResource.Loading -> {}
                is LocalResource.Error -> {
                    TextButton(onClick = { onModify(prompt) }) {
                        Text(stringResource(Res.string.ai_playlist_try_again))
                    }
                }

                else -> {
                    TextButton(onClick = { onModify(prompt) }, enabled = prompt.isNotBlank()) {
                        Text(stringResource(Res.string.ai_playlist_generate))
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        },
    )
}
