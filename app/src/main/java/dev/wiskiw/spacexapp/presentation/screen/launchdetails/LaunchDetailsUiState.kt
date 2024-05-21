package dev.wiskiw.spacexapp.presentation.screen.launchdetails

import android.os.Parcelable
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchDetailsUiState(
    val launchId: String,
    val isLoading: Boolean,
    val error: String?,
    val details: LaunchDetailsFull?,
) : Parcelable
