package dev.wiskiw.spacexapp.presentation.tool.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<State : Any, Action : MviAction, SideEffect : MviSideEffect> :
    ViewModel() {

    private val sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffect: Flow<SideEffect>
        get() = sideEffectChannel.receiveAsFlow()


    private val initialUiState: State by lazy { createInitialUiState() }
    private val internalUiStateFlow = MutableStateFlow(initialUiState)
    val uiStateFlow: StateFlow<State> = internalUiStateFlow.asStateFlow()

    abstract fun createInitialUiState(): State

    protected fun updateState(updater: State.() -> State) {
        internalUiStateFlow.update {
            updater.invoke(it)
        }
    }

    abstract fun handleAction(action: Action)

    protected fun sendSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            sideEffectChannel.send(sideEffect)
        }
    }
}
