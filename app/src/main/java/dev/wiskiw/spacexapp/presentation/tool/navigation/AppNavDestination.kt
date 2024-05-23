package dev.wiskiw.spacexapp.presentation.tool.navigation

import kotlinx.serialization.Serializable

internal sealed interface AppNavDestination {

    @Serializable
    data object Launches : AppNavDestination

    @Serializable
    data class LaunchDetails(
        val launchId: String,
    ) : AppNavDestination
}
