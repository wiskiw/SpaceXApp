package dev.wiskiw.spacexapp.presentation.tool

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.wiskiw.spacexapp.presentation.screen.launchdetails.LaunchDetailsScreen
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class NavDestination {

    @Serializable
    data object Launches : NavDestination()

    @Serializable
    data class LaunchDetails(
        val launchId: String,
    ) : NavDestination()
}

fun NavGraphBuilder.buildGraph(navController: NavController) = run {
    composable<NavDestination.Launches> {
        LaunchesScreen(
            navigateTo = navController::navigate,
        )
    }

    composable<NavDestination.LaunchDetails> {
        val args = it.toRoute<NavDestination.LaunchDetails>()
        LaunchDetailsScreen(
            launchId = args.launchId,
            navigateBack = { navController.popBackStack() },
        )

    }
}
