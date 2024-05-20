package dev.wiskiw.spacexapp.data.repository

import dev.wiskiw.spacexapp.data.datasource.SpaceXLaunchesRemoteDataSource
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository

class SpaceXLaunchesRepository(
    // todo SpaceXLaunchesRemoteDataSource can be replaced with an interface
    private val remoteDataSource: SpaceXLaunchesRemoteDataSource,
) : LaunchesRepository {

    override suspend fun getLaunchList(): Result<List<LaunchDetailsShort>> = runCatching {
        remoteDataSource.getLaunchList()
    }

    override suspend fun getLaunchDetails(id: String): Result<LaunchDetailsFull> = runCatching {
        remoteDataSource.getLaunch(id = id)
    }
}
