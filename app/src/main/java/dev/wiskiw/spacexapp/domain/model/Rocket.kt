package dev.wiskiw.spacexapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rocket(
    val id: String,
    val name: String,
    val type: String,
) : Parcelable
