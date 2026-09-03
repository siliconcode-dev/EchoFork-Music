package echo.music.enhanced.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.ChipColors
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import echo.music.enhanced.ui.icon.ArrowBackIosNew
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.theme.typo
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.third_party_libraries
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/** Shared "Third party libraries" sheet — used from both Classic's inline About section and Better Echo's dedicated About screen. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdPartyLibrariesSheet(
    innerPadding: PaddingValues,
    onDismiss: () -> Unit,
) {
    val libraries by produceLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }
    val lazyListState = rememberLazyListState()
    val canScrollBackward by remember {
        derivedStateOf {
            lazyListState.canScrollBackward
        }
    }
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = {
                !canScrollBackward
            },
        )
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {},
        scrimColor = Color.Black.copy(alpha = .5f),
        sheetState = sheetState,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
        shape = RectangleShape,
    ) {
        // Capture theme colors here: the ChipColors getters below run outside composition.
        val surfaceContainerHighestColor = MaterialTheme.colorScheme.surfaceContainerHighest
        val onSurfaceColor = MaterialTheme.colorScheme.onSurface
        LibrariesContainer(
            libraries?.copy(
                libraries =
                    libraries
                        ?.libraries
                        ?.distinctBy {
                            it.name
                        }?.toImmutableList() ?: emptyList<Library>().toImmutableList(),
            ),
            Modifier.fillMaxSize(),
            lazyListState = lazyListState,
            contentPadding = innerPadding,
            colors =
                LibraryDefaults.libraryColors(
                    licenseChipColors =
                        object : ChipColors {
                            override val containerColor: Color
                                get() = surfaceContainerHighestColor
                            override val contentColor: Color
                                get() = onSurfaceColor
                        },
                ),
            header = {
                item {
                    TopAppBar(
                        windowInsets = WindowInsets(0, 0, 0, 0),
                        title = {
                            Text(
                                text = stringResource(Res.string.third_party_libraries),
                                style = typo().titleMedium,
                            )
                        },
                        navigationIcon = {
                            Box(Modifier.padding(horizontal = 5.dp)) {
                                RippleIconButton(
                                    echoIcons.ArrowBackIosNew,
                                    Modifier.size(32.dp),
                                    true,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                ) {
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        onDismiss()
                                    }
                                }
                            }
                        },
                    )
                }
            },
        )
    }
}
