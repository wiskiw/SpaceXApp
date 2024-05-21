package dev.wiskiw.spacexapp.presentation.tool.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow


@Composable
fun <SideEffect : MviSideEffect> ConsumeSideEffect(
    sideEffectFlow: Flow<SideEffect>,
    lifeCycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffectHandler: (SideEffect) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow) {
        lifecycleOwner.repeatOnLifecycle(lifeCycleState) {
            sideEffectFlow.collect(sideEffectHandler)
        }
    }
}

@Composable
fun <Action : MviAction, SideEffect : MviSideEffect> ConsumeSideEffect(
    viewModel: MviViewModel<Action, SideEffect>,
    lifeCycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffectHandler: (SideEffect) -> Unit,
) {
    ConsumeSideEffect(
        sideEffectFlow = viewModel.sideEffect,
        lifeCycleState = lifeCycleState,
        sideEffectHandler = sideEffectHandler,
    )
}
