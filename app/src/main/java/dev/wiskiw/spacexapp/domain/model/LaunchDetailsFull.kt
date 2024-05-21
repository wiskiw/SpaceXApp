package dev.wiskiw.spacexapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchDetailsFull(
    val id: String,
    val rocket: Rocket,
    val mission: Mission,
    val site: String,
) : Parcelable
