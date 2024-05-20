package dev.wiskiw.spacexapp.data.datasource

import com.apollographql.apollo3.ApolloClient
import dev.wiskiw.spacexapp.LaunchDetailsQuery
import dev.wiskiw.spacexapp.LaunchesQuery
import dev.wiskiw.spacexapp.data.safeApolloRequest
import dev.wiskiw.spacexapp.repository.model.DataSourceException

class SpaceXLaunchesRemoteDataSource(
    private val apolloClient: ApolloClient,
) {

    @Throws(DataSourceException::class)
    suspend fun getLaunches(): List<LaunchesQuery.Launch> = safeApolloRequest {
        val requestQuery = LaunchesQuery()
        val response = apolloClient.query(requestQuery).execute()
        return@safeApolloRequest response.data?.launches?.launches?.filterNotNull() ?: emptyList()
    }

    @Throws(DataSourceException::class)
    suspend fun getLaunch(id: String): LaunchDetailsQuery.Launch = safeApolloRequest {
        val requestQuery = LaunchDetailsQuery(
            launchId = id,
        )
        val response = apolloClient.query(requestQuery).execute()
        return@safeApolloRequest response.data?.launch
            ?: throw DataSourceException("Launch with id=$id not found")
    }
}
