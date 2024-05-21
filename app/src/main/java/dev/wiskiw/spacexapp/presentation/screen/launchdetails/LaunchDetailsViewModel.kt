package dev.wiskiw.spacexapp.presentation.screen.launchdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.domain.usecase.LaunchDetailsUseCase
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviAction
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.spacexapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchDetailsViewModel(
    private val detailsUseCase: LaunchDetailsUseCase,
    private val logger: AppLogger,
) :
    MviViewModel<LaunchDetailsViewModel.Action, LaunchDetailsViewModel.SideEffect>() {

    sealed interface Action : MviAction {
        data object OnBackClick : Action
        data object OnRetryClick : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data object NavigateBack : SideEffect
    }

    var uiState: LaunchDetailsUiState by mutableStateOf(
        LaunchDetailsUiState(
            launchId = "",
            isLoading = true,
            error = null,
            details = null,
        )
    )

    fun onArgsReceived(
        launchId: String,
    ) {
        uiState = uiState.copy(
            launchId = launchId,
        )
        fetchDetails(launchId)
    }

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClick -> sendSideEffect(SideEffect.NavigateBack)
            Action.OnRetryClick -> {
                if (uiState.launchId.isNotBlank()) {
                    fetchDetails(uiState.launchId)
                } else {
                    logger.logError("Unexpected state, launchId is empty")
                }
            }
        }
    }

    private fun fetchDetails(launchId: String) {
        uiState = uiState.copy(
            isLoading = true,
            error = null,
            details = null,
        )

        viewModelScope.launch {
            detailsUseCase.get(id = launchId)
                .catch {
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            details = null,
                            isLoading = false,
                            error = it.message,
                        )
                    }
                }
                .collectLatest { details ->
                    withContext(Dispatchers.Main) {
                        uiState = uiState.copy(
                            details = details,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }
}
