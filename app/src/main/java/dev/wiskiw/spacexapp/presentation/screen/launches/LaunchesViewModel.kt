package dev.wiskiw.spacexapp.presentation.screen.launches

import androidx.lifecycle.viewModelScope
import dev.wiskiw.spacexapp.domain.usecase.LaunchListUseCase
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviAction
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LaunchesViewModel(
    private val launchListUseCase: LaunchListUseCase,
) : MviViewModel<LaunchesUiState, LaunchesViewModel.Action, LaunchesViewModel.SideEffect>() {

    sealed interface Action : MviAction {
        data class OnLaunchClick(val launchId: String) : Action
        data object OnRetryClick : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data class NavigateToLaunchDetails(val launchId: String) : SideEffect
    }


    init {
        fetchLaunches()
    }

    override fun createInitialUiState() = LaunchesUiState(
        isLoading = false,
        error = null,
        launches = emptyList(),
    )

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
        updateState {
            copy(
                launches = emptyList(),
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            launchListUseCase.get()
                .catch {
                    updateState {
                        copy(
                            launches = emptyList(),
                            isLoading = false,
                            error = it.message,
                        )
                    }
                }
                .collectLatest { launches ->
                    updateState {
                        copy(
                            launches = launches,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }
}
