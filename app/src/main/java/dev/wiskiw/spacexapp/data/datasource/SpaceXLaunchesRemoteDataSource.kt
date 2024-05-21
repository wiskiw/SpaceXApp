package dev.wiskiw.spacexapp.data.datasource

import dev.wiskiw.spacexapp.data.model.DataSourceException
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort

interface SpaceXLaunchesRemoteDataSource {

    @Throws(DataSourceException::class)
    suspend fun getLaunchList(): List<LaunchDetailsShort>

    @Throws(DataSourceException::class)
    suspend fun getLaunch(id: String): LaunchDetailsFull

}
