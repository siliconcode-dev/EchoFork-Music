package echo.music.enhanced.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import echo.music.enhanced.ui.icon.AutoGraph
import echo.music.enhanced.ui.icon.Speed
import echo.music.enhanced.ui.icon.SpatialAudio
import echo.music.enhanced.ui.icon.SurroundSound
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.theme.typo
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.whats_new_dismiss
import echomusic.composeapp.generated.resources.whats_new_immersive_audio_description
import echomusic.composeapp.generated.resources.whats_new_immersive_audio_title
import echomusic.composeapp.generated.resources.whats_new_spatial_audio_description
import echomusic.composeapp.generated.resources.whats_new_spatial_audio_title
import echomusic.composeapp.generated.resources.whats_new_title
import echomusic.composeapp.generated.resources.whats_new_true_motion_description
import echomusic.composeapp.generated.resources.whats_new_true_motion_title
import echomusic.composeapp.generated.resources.whats_new_version_format
import echomusic.composeapp.generated.resources.whats_new_wavy_description
import echomusic.composeapp.generated.resources.whats_new_wavy_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class WhatsNewEntry(
    val icon: ImageVector,
    val title: StringResource,
    val description: StringResource,
)

private val whatsNewEntries =
    listOf(
        WhatsNewEntry(echoIcons.SpatialAudio, Res.string.whats_new_spatial_audio_title, Res.string.whats_new_spatial_audio_description),
        WhatsNewEntry(
            echoIcons.SurroundSound,
            Res.string.whats_new_immersive_audio_title,
            Res.string.whats_new_immersive_audio_description,
        ),
        WhatsNewEntry(echoIcons.Speed, Res.string.whats_new_true_motion_title, Res.string.whats_new_true_motion_description),
        WhatsNewEntry(echoIcons.AutoGraph, Res.string.whats_new_wavy_title, Res.string.whats_new_wavy_description),
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNewDialog(
    versionName: String,
    onDismiss: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BasicAlertDialog(
            onDismissRequest = onDismiss,
            modifier = Modifier.wrapContentSize(),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                color = rememberSurfaceDarkColors().container,
                contentColor = rememberSurfaceDarkColors().content,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                shadowElevation = 1.dp,
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(Res.string.whats_new_title),
                        style = typo().headlineMedium,
                    )
                    Text(
                        text = stringResource(Res.string.whats_new_version_format, versionName),
                        style = typo().bodyMedium,
                    )
                    Spacer(Modifier.height(20.dp))
                    Column {
                        whatsNewEntries.forEach { entry ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                verticalAlignment = Alignment.Top,
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(40.dp),
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = entry.icon,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.size(20.dp),
                                        )
                                    }
                                }
                                Spacer(Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(entry.title),
                                        style = typo().titleSmall,
                                    )
                                    Text(
                                        text = stringResource(entry.description),
                                        style = typo().bodySmall,
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(Res.string.whats_new_dismiss))
                    }
                }
            }
        }
    }
}
