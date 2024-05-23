package dev.wiskiw.spacexapp.presentation.tool.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * This file contains extension functions for Compose Navigation.
 * It helps to use type-safe arguments in navigation destinations.
 *
 * The Compose Navigation Library (since version 2.8.0-alpha) has built-in type-safe arguments.
 * However, the current version (2.8.0-beta01) crashes after the app process is killed.
 */

internal const val ARGS_KEY = "ARGS_KEY"

internal inline fun <reified T : TypeSafeNavDestination> NavGraphBuilder.composableTypeSafeNavDestination(
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit),
) {
    val route = buildTypeSafeNavDestinationRoute<T>()
    composable(route, content = content)
}

internal inline fun <reified T : TypeSafeNavDestination> NavController.navigateToTypeSafeNavDestination(
    destination: T,
) {
    val stringArgs = Json.encodeToString(destination)
    val route = buildTypeSafeNavDestinationRoute<T>()
    val routeWithArgs = route.replace("{$ARGS_KEY}", stringArgs)
    navigate(routeWithArgs)
}

internal inline fun <reified T : TypeSafeNavDestination> NavBackStackEntry.getTypeSafeNavDestination(): T {
    val stringArgs = arguments?.getString(ARGS_KEY)
        ?: throw IllegalArgumentException("Expected nav arguments are missing. Please use navigateToTypeSafeNavDestination(T) to navigate to a destination.")
    return Json.decodeFromString<T>(stringArgs)
}

internal inline fun <reified T : TypeSafeNavDestination> buildTypeSafeNavDestinationRoute(): String {
    val destinationName = getTypeSafeNavDestinationName<T>()
    return buildTypeSafeNavDestinationRoute(destinationName)
}

private inline fun <reified T : TypeSafeNavDestination> getTypeSafeNavDestinationName(): String {
    return T::class.qualifiedName.toString()
}

private fun buildTypeSafeNavDestinationRoute(appNavDestinationName: String): String {
    return "$appNavDestinationName/{$ARGS_KEY}"
}
