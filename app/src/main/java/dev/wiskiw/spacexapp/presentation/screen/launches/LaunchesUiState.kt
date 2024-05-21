package dev.wiskiw.spacexapp.presentation.screen.launches

import android.os.Parcelable
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchesUiState(
    val isLoading: Boolean,
    val error: String?,
    val launches: List<LaunchDetailsShort>,
) : Parcelable
