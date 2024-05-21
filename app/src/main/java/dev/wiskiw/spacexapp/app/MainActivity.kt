package dev.wiskiw.spacexapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.wiskiw.spacexapp.presentation.tool.NavDestination
import dev.wiskiw.spacexapp.presentation.tool.buildGraph
import dev.wiskiw.spacexapp.presentation.theme.SpaceXAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceXAppTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavDestination.Launches
                ) {
                    with(this) { buildGraph(navController) }
                }
            }
        }
    }
}
