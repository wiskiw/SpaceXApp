package dev.wiskiw.spacexapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.wiskiw.spacexapp.presentation.theme.SpaceXAppTheme
import dev.wiskiw.spacexapp.presentation.tool.navigation.AppNavDestination
import dev.wiskiw.spacexapp.presentation.tool.navigation.buildGraph
import dev.wiskiw.spacexapp.presentation.tool.navigation.buildTypeSafeNavDestinationRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceXAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = buildTypeSafeNavDestinationRoute<AppNavDestination.Launches>(),
                ) {
                    with(this) { buildGraph(navController) }
                }
            }
        }
    }
}
