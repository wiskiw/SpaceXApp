package dev.wiskiw.spacexapp.data.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import dev.wiskiw.spacexapp.LaunchDetailsQuery
import dev.wiskiw.spacexapp.LaunchesQuery
import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.data.safeApolloRequest
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.data.mapper.ApolloLaunchDetailsToLaunchDetailsFullMapper
import dev.wiskiw.spacexapp.data.mapper.ApolloLaunchesToLaunchDetailsShortMapper
import dev.wiskiw.spacexapp.data.model.DataSourceException
import dev.wiskiw.spacexapp.data.model.MapperException

class SpaceXLaunchesRemoteDataSource(
    private val apolloClient: ApolloClient,
    private val logger: AppLogger,
) {

    private val launchesMapper by lazy { ApolloLaunchesToLaunchDetailsShortMapper() }
    private val launchDetailsMapper by lazy { ApolloLaunchDetailsToLaunchDetailsFullMapper() }

    @Throws(DataSourceException::class)
    suspend fun getLaunchList(): List<LaunchDetailsShort> {
        val requestQuery = LaunchesQuery()
        val response = safeApolloRequest {
            apolloClient.query(requestQuery).execute()
        }

        return response.data
            ?.launches
            ?.launches
            ?.filterNotNull()
            ?.mapNotNull {
                try {
                    launchesMapper.map(it)
                } catch (mapException: MapperException) {
                    logger.logError(
                        message = "Failed to map to LaunchDetailsShort (launchId=${it.id})",
                        exception = mapException,
                    )
                    null
                }
            }
            ?: emptyList()
    }

    @Throws(DataSourceException::class)
    suspend fun getLaunch(id: String): LaunchDetailsFull {
        val requestQuery = LaunchDetailsQuery(
            launchId = id,
        )
        val response = safeApolloRequest {
            apolloClient.query(requestQuery).execute()
        }

        val apolloLaunch = response.data?.launch
            ?: throw DataSourceException("Launch with id=$id not found")

        try {
            return launchDetailsMapper.map(apolloLaunch)
        } catch (mapException: MapperException) {
            throw DataSourceException(mapException)
        }
    }
}
