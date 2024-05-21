package dev.wiskiw.spacexapp.data

import com.google.common.truth.Truth.assertThat
import dev.wiskiw.spacexapp.data.datasource.SpaceXLaunchesRemoteDataSource
import dev.wiskiw.spacexapp.data.model.DataSourceException
import dev.wiskiw.spacexapp.data.repository.SpaceXLaunchesRepository
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test


class SpaceXLaunchesRepositoryTest {

    @Test
    fun getLaunchListReturnsResultSuccessWhenNoExceptionInDataSource() = runTest {
        val list = mockk<List<LaunchDetailsShort>>()
        val dataSource = mockk<SpaceXLaunchesRemoteDataSource>()
        coEvery { dataSource.getLaunchList() } returns list
        val repository = SpaceXLaunchesRepository(
            remoteDataSource = dataSource,
        )

        val launchListResult = repository.getLaunchList()

        assertThat(launchListResult.isSuccess).isTrue()
        assertThat(launchListResult.getOrNull()).isEqualTo(list)
    }

    @Test
    fun getLaunchListReturnsResultErrorWhenDataSourceThrowsException() = runTest {
        val exception = IllegalArgumentException("IllegalArgumentException in getLaunchList")
        val dataSource = mockk<SpaceXLaunchesRemoteDataSource>()
        coEvery { dataSource.getLaunchList() } throws exception
        val repository = SpaceXLaunchesRepository(
            remoteDataSource = dataSource,
        )

        val launchListResult = repository.getLaunchList()

        assertThat(launchListResult.isFailure).isTrue()
        assertThat(launchListResult.exceptionOrNull()).isEqualTo(exception)
    }


    @Test
    fun getLaunchDetailsReturnsResultSuccessWhenNoExceptionInDataSource() = runTest {
        val launch = mockk<LaunchDetailsFull>()
        val dataSource = mockk<SpaceXLaunchesRemoteDataSource>()
        coEvery { dataSource.getLaunch(any()) } returns launch
        val repository = SpaceXLaunchesRepository(
            remoteDataSource = dataSource,
        )

        val launchListResult = repository.getLaunchDetails("launchId")

        assertThat(launchListResult.isSuccess).isTrue()
        assertThat(launchListResult.getOrNull()).isEqualTo(launch)
    }

    @Test
    fun getLaunchDetailsCallsCorrespondingDataSourceMethod() = runTest {
        val launchId = "launch ID 123"
        val launch = mockk<LaunchDetailsFull>()
        val dataSource = mockk<SpaceXLaunchesRemoteDataSource>()
        coEvery { dataSource.getLaunch(launchId) } returns launch
        val repository = SpaceXLaunchesRepository(
            remoteDataSource = dataSource,
        )

        repository.getLaunchDetails(launchId)

        coVerify { dataSource.getLaunch(eq(launchId)) }
    }

    @Test
    fun getLaunchDetailsReturnsResultErrorWhenDataSourceThrowsException() = runTest {
        val exception = DataSourceException("DataSourceException in getLaunch")
        val dataSource = mockk<SpaceXLaunchesRemoteDataSource>()
        coEvery { dataSource.getLaunch(any()) } throws exception
        val repository = SpaceXLaunchesRepository(
            remoteDataSource = dataSource,
        )

        val launchDetailsResult = repository.getLaunchDetails("launchId")

        assertThat(launchDetailsResult.isFailure).isTrue()
        assertThat(launchDetailsResult.exceptionOrNull()).isEqualTo(exception)
    }
}
