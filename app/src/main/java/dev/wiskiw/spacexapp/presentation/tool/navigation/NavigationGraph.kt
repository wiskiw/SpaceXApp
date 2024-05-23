package dev.wiskiw.spacexapp.presentation.tool.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import dev.wiskiw.spacexapp.presentation.screen.launchdetails.LaunchDetailsScreen
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesScreen


fun NavGraphBuilder.buildGraph(navController: NavController) {
    composableTypeSafeNavDestination<AppNavDestination.Launches> {
        LaunchesScreen(
            navigateToDetails = { launchId ->
                val destination = AppNavDestination.LaunchDetails(
                    launchId = launchId,
                )
                navController.navigateToTypeSafeNavDestination(destination)
            },
        )
    }

    composableTypeSafeNavDestination<AppNavDestination.LaunchDetails> {
        val destination = it.getTypeSafeNavDestination<AppNavDestination.LaunchDetails>()
        LaunchDetailsScreen(
            launchId = destination.launchId,
            navigateBack = { navController.popBackStack() },
        )
    }
}
