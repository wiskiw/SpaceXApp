package dev.wiskiw.spacexapp.presentation.screen.launches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.model.Mission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchesViewModel : ViewModel() {

    sealed interface Action {
        data class OnLaunchClick(val launchId: String) : Action
    }

    var uiState: LaunchesUiState by mutableStateOf(
        LaunchesUiState(
            launches = emptyList(),
        )
    )

    init {
        fetchLaunches()
    }

    fun handleAction(action: Action) {
        // todo
    }

    private fun fetchLaunches() {
        viewModelScope.launch {
            val launches = listOf(
                LaunchDetailsShort(
                    id = "110",
                    rocketName = "Falcon 9",
                    mission = Mission(
                        name = "Starlink-15",
                        imageUrl = "https://imgur.com/E7fjUBD.png"
                    )
                ),
                LaunchDetailsShort(
                    id = "9",
                    rocketName = "Falcon Heavy",
                    mission = Mission(
                        name = "Tesla Plaid",
                        imageUrl = "https://images2.imgbox.com/d2/3b/bQaWiil0_o.png"
                    )
                )
            )

            withContext(Dispatchers.Main){
                uiState = uiState.copy(launches = launches)
            }
        }
    }
}
