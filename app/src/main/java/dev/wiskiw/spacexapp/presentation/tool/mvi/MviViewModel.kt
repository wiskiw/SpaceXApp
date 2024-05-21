package dev.wiskiw.spacexapp.presentation.tool.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MviViewModel<Action : MviAction, SideEffect : MviSideEffect> : ViewModel() {

    private val sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffect: Flow<SideEffect>
        get() = sideEffectChannel.receiveAsFlow()


    protected fun sendSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            sideEffectChannel.send(sideEffect)
        }
    }

    abstract fun handleAction(action: Action)
}
