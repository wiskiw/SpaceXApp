package dev.wiskiw.spacexapp.presentation.screen.launches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.wiskiw.spacexapp.domain.usecase.LaunchListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchesViewModel(
    private val launchListUseCase: LaunchListUseCase,
) : ViewModel() {

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
            // todo handle error state
            // todo handle loading state
            launchListUseCase.get()
                .collectLatest { launches ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(launches = launches)
                    }
                }
        }
    }
}
