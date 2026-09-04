package echo.music.enhanced.ui.navigation.destination.home

import kotlinx.serialization.Serializable

@Serializable
data class SettingsDestination(
    val highlightSection: String? = null,
)