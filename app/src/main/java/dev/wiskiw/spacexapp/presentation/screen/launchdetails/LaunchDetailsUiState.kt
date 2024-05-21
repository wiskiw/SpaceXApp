package dev.wiskiw.spacexapp.presentation.screen.launchdetails

import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull

data class LaunchDetailsUiState(
    val launchId: String,

    val isLoading: Boolean,
    val error: String?,
    val details: LaunchDetailsFull?,
)
