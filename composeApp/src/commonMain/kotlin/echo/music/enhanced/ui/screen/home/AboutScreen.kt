package echo.music.enhanced.ui.screen.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import echo.music.enhanced.domain.manager.DataStoreManager
import echo.music.enhanced.ui.component.RippleIconButton
import echo.music.enhanced.ui.component.Material3SettingsGroup
import echo.music.enhanced.ui.component.ScallopedShape
import echo.music.enhanced.ui.component.ThirdPartyLibrariesSheet
import echo.music.enhanced.ui.component.WavyDivider
import echo.music.enhanced.ui.icon.ArrowBackIosNew
import echo.music.enhanced.ui.icon.Info
import echo.music.enhanced.ui.icon.LibraryMusic
import echo.music.enhanced.ui.icon.LogoDev
import echo.music.enhanced.ui.icon.echoIcons
import echo.music.enhanced.ui.theme.typo
import echo.music.enhanced.utils.VersionManager
import echomusic.composeapp.generated.resources.Res
import echomusic.composeapp.generated.resources.about_community_info
import echomusic.composeapp.generated.resources.about_flip_developed_by
import echomusic.composeapp.generated.resources.about_developer
import echomusic.composeapp.generated.resources.about_license
import echomusic.composeapp.generated.resources.about_license_description
import echomusic.composeapp.generated.resources.about_original_project
import echomusic.composeapp.generated.resources.about_us
import echomusic.composeapp.generated.resources.about_view_repository
import echomusic.composeapp.generated.resources.about_view_repository_description
import echomusic.composeapp.generated.resources.app_name
import echomusic.composeapp.generated.resources.based_on_description
import echomusic.composeapp.generated.resources.description_and_licenses
import echomusic.composeapp.generated.resources.third_party_libraries
import echomusic.composeapp.generated.resources.version_format
import org.jetbrains.compose.resources.stringResource

private const val REPO_URL = "https://github.com/siliconcode-dev/EchoFork-Music"
private const val DEVELOPER_GITHUB = "siliconcode-dev"

/**
 * Better Echo's dedicated About screen, replacing the flat inline "About us" list — built from
 * 2 reference screenshots the user pulled off an installed app (MELD) showing this exact layout:
 * a scalloped M3-Expressive badge header, a wavy divider, a Developer section, an "Original
 * Project" credit, and a "Community & Info" grouped card. Only reachable from Settings when
 * Interface mode is Better Echo — Classic keeps its inline section as-is.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController,
    innerPadding: PaddingValues,
) {
    val uriHandler = LocalUriHandler.current
    var showThirdPartyLibraries by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.about_us), style = typo().titleMedium) },
                navigationIcon = {
                    RippleIconButton(
                        onClick = { navController.popBackStack() },
                        imageVector = echoIcons.ArrowBackIosNew,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        var isEasterEggActive by remember { mutableStateOf(false) }
                        val flipRotation by animateFloatAsState(
                            targetValue = if (isEasterEggActive) 180f else 0f,
                            animationSpec =
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow,
                                ),
                            label = "aboutAvatarFlip",
                        )
                        val flipInteractionSource = remember { MutableInteractionSource() }
                        val isFlipPressed by flipInteractionSource.collectIsPressedAsState()
                        val flipPressScale by animateFloatAsState(
                            targetValue = if (isFlipPressed) 0.85f else 1f,
                            animationSpec =
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium,
                                ),
                            label = "aboutAvatarPressScale",
                        )
                        val density = LocalDensity.current
                        Box(
                            modifier =
                                Modifier
                                    .size(96.dp)
                                    .graphicsLayer {
                                        rotationY = flipRotation
                                        scaleX = flipPressScale
                                        scaleY = flipPressScale
                                        cameraDistance = 12f * density.density
                                    }.clip(ScallopedShape())
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .clickable(
                                        interactionSource = flipInteractionSource,
                                        indication = null,
                                        onClick = { isEasterEggActive = !isEasterEggActive },
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (flipRotation <= 90f) {
                                Icon(
                                    imageVector = echoIcons.LibraryMusic,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(40.dp),
                                )
                            } else {
                                AsyncImage(
                                    model = "https://github.com/$DEVELOPER_GITHUB.png",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .graphicsLayer { rotationY = 180f },
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text =
                                if (flipRotation <= 90f) {
                                    stringResource(Res.string.app_name)
                                } else {
                                    stringResource(Res.string.about_flip_developed_by, DEVELOPER_GITHUB)
                                },
                            style = typo().headlineSmall.copy(fontWeight = FontWeight.Black),
                        )
                        Spacer(Modifier.height(8.dp))
                        Card(
                            shape = RoundedCornerShape(50),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                        ) {
                            Text(
                                text = stringResource(Res.string.version_format, VersionManager.getVersionName()),
                                style = typo().labelMedium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            )
                        }
                    }
                }
            }

            item {
                WavyDivider(modifier = Modifier.padding(horizontal = 32.dp))
            }

            item {
                Column {
                    Text(
                        text = stringResource(Res.string.about_developer),
                        style = typo().labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(12.dp))
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = "https://github.com/$DEVELOPER_GITHUB.png",
                            contentDescription = null,
                            modifier = Modifier.size(88.dp).clip(CircleShape),
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(text = DEVELOPER_GITHUB, style = typo().titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Spacer(Modifier.height(16.dp))
                    Material3SettingsGroup(
                        interfaceMode = DataStoreManager.INTERFACE_BETTER_ECHO,
                        items =
                            listOf<@Composable () -> Unit> {
                                Row(
                                    modifier = Modifier.fillMaxWidth().clickable { uriHandler.openUri("https://github.com/$DEVELOPER_GITHUB") }.padding(vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Icon(imageVector = echoIcons.LogoDev, contentDescription = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text(text = "GitHub", style = typo().bodyMedium)
                                }
                            },
                    )
                }
            }

            item {
                Column {
                    Text(
                        text = stringResource(Res.string.about_original_project),
                        style = typo().labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = stringResource(Res.string.based_on_description), style = typo().bodyMedium)
                }
            }

            item {
                Column {
                    Text(
                        text = stringResource(Res.string.about_community_info),
                        style = typo().labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(12.dp))
                    val repoLabel = stringResource(Res.string.about_view_repository)
                    val repoDesc = stringResource(Res.string.about_view_repository_description)
                    val licenseLabel = stringResource(Res.string.about_license)
                    val licenseDesc = stringResource(Res.string.about_license_description)
                    val thirdPartyLabel = stringResource(Res.string.third_party_libraries)
                    val thirdPartyDesc = stringResource(Res.string.description_and_licenses)
                    Material3SettingsGroup(
                        interfaceMode = DataStoreManager.INTERFACE_BETTER_ECHO,
                        items =
                            listOf<@Composable () -> Unit>(
                                {
                                    AboutInfoRow(
                                        icon = echoIcons.LogoDev,
                                        title = repoLabel,
                                        subtitle = repoDesc,
                                        onClick = { uriHandler.openUri(REPO_URL) },
                                    )
                                },
                                {
                                    AboutInfoRow(
                                        icon = echoIcons.Info,
                                        title = licenseLabel,
                                        subtitle = licenseDesc,
                                        onClick = { uriHandler.openUri("$REPO_URL/blob/main/LICENSE") },
                                    )
                                },
                                {
                                    AboutInfoRow(
                                        icon = echoIcons.Info,
                                        title = thirdPartyLabel,
                                        subtitle = thirdPartyDesc,
                                        onClick = { showThirdPartyLibraries = true },
                                    )
                                },
                            ),
                    )
                }
            }
        }
    }

    if (showThirdPartyLibraries) {
        ThirdPartyLibrariesSheet(innerPadding = innerPadding, onDismiss = { showThirdPartyLibraries = false })
    }
}

@Composable
private fun AboutInfoRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = title, style = typo().bodyLarge)
            Text(text = subtitle, style = typo().bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
