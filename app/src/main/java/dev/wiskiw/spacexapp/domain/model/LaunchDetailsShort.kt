package dev.wiskiw.spacexapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LaunchDetailsShort(
    val id: String,
    val rocketName: String,
    val mission: Mission,
) : Parcelable
