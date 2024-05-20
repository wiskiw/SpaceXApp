package dev.wiskiw.spacexapp.repository

import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.data.datasource.SpaceXLaunchesRemoteDataSource
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import dev.wiskiw.spacexapp.repository.mapper.ApolloLaunchDetailsToLaunchDetailsFullMapper
import dev.wiskiw.spacexapp.repository.mapper.ApolloLaunchesToLaunchDetailsShortMapper
import dev.wiskiw.spacexapp.repository.model.MapperException

class SpaceXLaunchesRepository(
    // todo SpaceXLaunchesRemoteDataSource can be replaced with an interface
    private val remoteDataSource: SpaceXLaunchesRemoteDataSource,
    private val logger: AppLogger,
) : LaunchesRepository {

    private val launchesMapper by lazy { ApolloLaunchesToLaunchDetailsShortMapper() }
    private val launchDetailsMapper by lazy { ApolloLaunchDetailsToLaunchDetailsFullMapper() }

    override suspend fun getLaunchList(): Result<List<LaunchDetailsShort>> = runCatching {
        return@runCatching remoteDataSource.getLaunches()
            .mapNotNull {
                try {
                    launchesMapper.map(it)
                } catch (mapException: MapperException) {
                    logger.logError(
                        message = "Failed to map to LaunchDetailsShort (id=${it.id})",
                        exception = mapException,
                    )
                    null
                }
            }
    }

    override suspend fun getLaunchDetails(id: String): Result<LaunchDetailsFull> = runCatching {
        val apolloLaunch = remoteDataSource.getLaunch(id = id)
        return@runCatching launchDetailsMapper.map(apolloLaunch)
    }
}
