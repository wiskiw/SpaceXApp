package dev.wiskiw.spacexapp.domain.usecase

import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LaunchDetailsUseCase(
    private val launchesRepository: LaunchesRepository,
) {

    fun get(id: String): Flow<LaunchDetailsFull> = flow {
        val launchDetailsResult = launchesRepository.getLaunchDetails(id)
        launchDetailsResult.onSuccess { emit(it) }
        launchDetailsResult.onFailure { throw it }
    }
}
