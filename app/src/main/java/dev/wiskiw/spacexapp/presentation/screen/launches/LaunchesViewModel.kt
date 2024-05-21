package dev.wiskiw.spacexapp.presentation.screen.launches

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dev.wiskiw.spacexapp.domain.usecase.LaunchListUseCase
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviAction
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchesViewModel(
    private val launchListUseCase: LaunchListUseCase,
) : MviViewModel<LaunchesViewModel.Action, LaunchesViewModel.SideEffect>() {

    sealed interface Action : MviAction {
        data class OnLaunchClick(val launchId: String) : Action
        data object OnRetryClick : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data class NavigateToLaunchDetails(val launchId: String) : SideEffect
    }

    var uiState: LaunchesUiState by mutableStateOf(
        LaunchesUiState(
            isLoading = false,
            error = null,
            launches = emptyList(),
        )
    )

    init {
        fetchLaunches()
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnRetryClick -> fetchLaunches()
            is Action.OnLaunchClick -> {
                val sideEffect = SideEffect.NavigateToLaunchDetails(launchId = action.launchId)
                sendSideEffect(sideEffect)
            }
        }
    }

    private fun fetchLaunches() {
        uiState = uiState.copy(
            launches = emptyList(),
            isLoading = true,
            error = null,
        )

        viewModelScope.launch {
            launchListUseCase.get()
                .catch {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            launches = emptyList(),
                            isLoading = false,
                            error = it.message,
                        )
                    }
                }
                .collectLatest { launches ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            launches = launches,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }
}
