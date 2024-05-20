package dev.wiskiw.spacexapp.domain.usecase

import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LaunchesUseCase(
    private val launchesRepository: LaunchesRepository,
) {

    fun getLaunches(): Flow<List<LaunchDetailsShort>> = flow {
        val launchListResult = launchesRepository.getLaunchList()
        launchListResult.onSuccess { emit(it) }
        launchListResult.onFailure { throw it }
    }

    fun getLaunchDetails(id: String): Flow<LaunchDetailsFull> = flow {
        val launchDetailsResult = launchesRepository.getLaunchDetails(id)
        launchDetailsResult.onSuccess { emit(it) }
        launchDetailsResult.onFailure { throw it }
    }
}
