package dev.wiskiw.spacexapp.domain.repository

import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort

interface LaunchesRepository {

    suspend fun getLaunchList(): Result<List<LaunchDetailsShort>>

    suspend fun getLaunchDetails(id: String): Result<LaunchDetailsFull>

}
