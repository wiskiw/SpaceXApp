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
internal inline fun <reified T> getAppNavDestinationName(): String {
    return T::class.qualifiedName.toString()
}

internal fun buildRoute(appNavDestinationName: String): String {
    return "$appNavDestinationName/{$ARGS_KEY}"
}

internal inline fun <reified T> buildRoute(): String {
    val destinationName = getAppNavDestinationName<T>()
    return buildRoute(destinationName)
}

internal inline fun <reified T> NavGraphBuilder.composableWithAppNavDestination(
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit),
) {
    val route = buildRoute<T>()
    composable(route, content = content)
}

internal inline fun <reified T> NavBackStackEntry.getAppNavDestination(): T {
    val stringArgs = arguments?.getString(ARGS_KEY)
        ?: throw IllegalArgumentException("Expected nav arguments are missing. Please use navigateToAppNavDestination(T) to navigate to a destination.")
    return Json.decodeFromString<T>(stringArgs)
}


internal inline fun <reified T> NavController.navigateToAppNavDestination(destination: T) {
    val stringArgs = Json.encodeToString(destination)
    val route = buildRoute<T>()
    val routeWithArgs = route.replace("{$ARGS_KEY}", stringArgs)
    navigate(routeWithArgs)
}
