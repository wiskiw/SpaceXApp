package dev.wiskiw.spacexapp.domain.model

data class LaunchDetailsFull(
    val id: String,
    val rocket: Rocket,
    val mission: Mission,
    val site : String,
)
