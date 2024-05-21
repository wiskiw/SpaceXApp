package dev.wiskiw.spacexapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mission(
    val name: String,
    val imageUrl: String?,
) : Parcelable
