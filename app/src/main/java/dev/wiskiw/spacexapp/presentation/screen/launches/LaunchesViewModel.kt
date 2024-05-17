package dev.wiskiw.spacexapp.presentation.screen.launches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LaunchesViewModel : ViewModel() {

    sealed interface Action {
        data class OnLaunchClick(val launchId: String) : Action
    }

    var uiState: LaunchesUiState by mutableStateOf(
        LaunchesUiState(
            launches = emptyList(),
        )
    )
        private set


    fun handleAction(action: Action) {
        // todo
    }


}
