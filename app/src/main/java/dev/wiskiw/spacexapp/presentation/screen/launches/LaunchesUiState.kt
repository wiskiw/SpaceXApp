package dev.wiskiw.spacexapp.presentation.screen.launches

import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort

data class LaunchesUiState(
    val isLoading: Boolean,
    val error: String?,
    val launches: List<LaunchDetailsShort>,
)
