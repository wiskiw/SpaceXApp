package dev.wiskiw.spacexapp.domain.model

data class LaunchDetailsShort(
    val id: String,
    val rocketName: String,
    val mission: Mission,
)
