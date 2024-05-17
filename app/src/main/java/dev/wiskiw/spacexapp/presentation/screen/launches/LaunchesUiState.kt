package dev.wiskiw.spacexapp.presentation.screen.launches

import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort

data class LaunchesUiState(
    val launches: List<LaunchDetailsShort>,
)
