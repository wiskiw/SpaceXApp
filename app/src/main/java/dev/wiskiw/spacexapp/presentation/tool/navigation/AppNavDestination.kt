package dev.wiskiw.spacexapp.presentation.tool.navigation

import kotlinx.serialization.Serializable

object AppNavDestination {

    @Serializable
    data object Launches : TypeSafeNavDestination

    @Serializable
    data class LaunchDetails(
        val launchId: String,
    ) : TypeSafeNavDestination
}
