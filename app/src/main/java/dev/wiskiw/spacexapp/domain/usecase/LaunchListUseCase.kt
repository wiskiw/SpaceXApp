package dev.wiskiw.spacexapp.domain.usecase

import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LaunchListUseCase(
    private val launchesRepository: LaunchesRepository,
) {

    fun get(): Flow<List<LaunchDetailsShort>> = flow {
        val launchListResult = launchesRepository.getLaunchList()
        launchListResult.onSuccess { emit(it) }
        launchListResult.onFailure { throw it }
    }
}
