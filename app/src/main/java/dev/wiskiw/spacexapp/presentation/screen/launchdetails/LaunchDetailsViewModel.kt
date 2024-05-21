package dev.wiskiw.spacexapp.presentation.screen.launchdetails

import androidx.lifecycle.viewModelScope
import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.domain.usecase.LaunchDetailsUseCase
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviAction
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LaunchDetailsViewModel(
    private val detailsUseCase: LaunchDetailsUseCase,
    private val logger: AppLogger,
) : MviViewModel<LaunchDetailsUiState, LaunchDetailsViewModel.Action, LaunchDetailsViewModel.SideEffect>() {

    sealed interface Action : MviAction {
        data object OnBackClick : Action
        data object OnRetryClick : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data object NavigateBack : SideEffect
    }

    override fun createInitialUiState() = LaunchDetailsUiState(
        launchId = "",
        isLoading = true,
        error = null,
        details = null,
    )

    fun onArgsReceived(
        launchId: String,
    ) {
        // fetch data only if launchId changed
        if (uiStateFlow.value.launchId != launchId) {
            updateState {
                copy(
                    launchId = launchId,
                )
            }
            fetchDetails(launchId)
        }
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClick -> sendSideEffect(SideEffect.NavigateBack)
            Action.OnRetryClick -> {
                if (uiStateFlow.value.launchId.isNotBlank()) {
                    fetchDetails(uiStateFlow.value.launchId)
                } else {
                    logger.logError("Unexpected state, launchId is empty")
                }
            }
        }
    }

    private fun fetchDetails(launchId: String) {
        updateState {
            copy(
                isLoading = true,
                error = null,
                details = null,
            )
        }

        viewModelScope.launch {
            detailsUseCase.get(id = launchId)
                .catch { exception ->
                    updateState {
                        copy(
                            details = null,
                            isLoading = false,
                            error = exception.message,
                        )
                    }
                }
                .collectLatest { details ->
                    updateState {
                        copy(
                            details = details,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }
}
