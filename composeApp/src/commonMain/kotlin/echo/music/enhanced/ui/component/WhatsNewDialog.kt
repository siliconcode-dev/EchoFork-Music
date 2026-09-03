package echo.music.enhanced.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import echo.music.enhanced.ui.icon.CheckCircle
import echo.music.enhanced.ui.icon.Info
import echo.music.enhanced.ui.icon.Sensors
import echo.music.enhanced.ui.icon.Tune
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.theme.typo
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.whats_new_about_page_description
import echomusic.composeapp.generated.resources.whats_new_about_page_title
import echomusic.composeapp.generated.resources.whats_new_better_echo_look_description
import echomusic.composeapp.generated.resources.whats_new_better_echo_look_title
import echomusic.composeapp.generated.resources.whats_new_dismiss
import echomusic.composeapp.generated.resources.whats_new_liquid_glass_expansion_description
import echomusic.composeapp.generated.resources.whats_new_liquid_glass_expansion_title
import echomusic.composeapp.generated.resources.whats_new_multiselect_description
import echomusic.composeapp.generated.resources.whats_new_multiselect_title
import echomusic.composeapp.generated.resources.whats_new_title
import echomusic.composeapp.generated.resources.whats_new_version_format
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class WhatsNewEntry(
    val icon: ImageVector,
    val title: StringResource,
    val description: StringResource,
)

private val whatsNewEntries =
    listOf(
        WhatsNewEntry(echoIcons.Tune, Res.string.whats_new_better_echo_look_title, Res.string.whats_new_better_echo_look_description),
        WhatsNewEntry(echoIcons.CheckCircle, Res.string.whats_new_multiselect_title, Res.string.whats_new_multiselect_description),
        WhatsNewEntry(echoIcons.Info, Res.string.whats_new_about_page_title, Res.string.whats_new_about_page_description),
        WhatsNewEntry(
            echoIcons.Sensors,
            Res.string.whats_new_liquid_glass_expansion_title,
            Res.string.whats_new_liquid_glass_expansion_description,
        ),
    )

// M3 Expressive spring: a bouncier, livelier feel than the platform default ease curves.
private val expressiveOffsetSpring =
    spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)

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
            var dialogVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { dialogVisible = true }
            AnimatedVisibility(
                visible = dialogVisible,
                enter = fadeIn(tween(220)) + slideInVertically(expressiveOffsetSpring) { fullHeight -> fullHeight / 3 },
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    color = rememberSurfaceDarkColors().container,
                    contentColor = rememberSurfaceDarkColors().content,
                    tonalElevation = AlertDialogDefaults.TonalElevation,
                    shadowElevation = 1.dp,
                ) {
                    Column(Modifier.padding(24.dp)) {
                        Text(
                            text = stringResource(Res.string.whats_new_title),
                            style = typo().headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(Res.string.whats_new_version_format, versionName),
                            style = typo().bodyMedium,
                        )
                        Spacer(Modifier.height(20.dp))
                        Column {
                            whatsNewEntries.forEachIndexed { index, entry ->
                                var rowVisible by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    delay(80L * index)
                                    rowVisible = true
                                }
                                AnimatedVisibility(
                                    visible = rowVisible,
                                    enter = fadeIn(tween(220)) + slideInHorizontally(expressiveOffsetSpring) { it / 4 },
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.Top,
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            modifier = Modifier.size(44.dp),
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = entry.icon,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    modifier = Modifier.size(22.dp),
                                                )
                                            }
                                        }
                                        Spacer(Modifier.width(16.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stringResource(entry.title),
                                                style = typo().titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                            )
                                            Text(
                                                text = stringResource(entry.description),
                                                style = typo().bodySmall,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        ) {
                            Text(stringResource(Res.string.whats_new_dismiss))
                        }
                    }
                }
            }
        }
    }
}
