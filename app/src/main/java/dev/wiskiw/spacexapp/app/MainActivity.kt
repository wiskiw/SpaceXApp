package dev.wiskiw.spacexapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesScreen
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesViewModel
import dev.wiskiw.spacexapp.presentation.theme.SpaceXAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceXAppTheme {


                // todo implement navigation
                LaunchesScreen()
            }
        }
    }
}
