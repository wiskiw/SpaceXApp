package dev.wiskiw.spacexapp.domain

import com.google.common.truth.Truth.assertThat
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import dev.wiskiw.spacexapp.domain.usecase.LaunchListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LaunchListUseCaseTest {

    @Test
    fun emitsValueFromResultWhenRepositoryReturnsResultSuccess() = runTest {
        val launches = mockk<List<LaunchDetailsShort>>()
        val successResult = Result.success(launches)
        val repository = mockk<LaunchesRepository>()
        coEvery { repository.getLaunchList() } returns successResult
        val useCase = LaunchListUseCase(launchesRepository = repository)

        val launchesFlow = useCase.get()

        launchesFlow.collectLatest {
            assertThat(it).isEqualTo(launches)
        }
    }

    @Test
    fun throwExceptionWhenRepositoryReturnsResultFailed() = runTest {
        val exception = IllegalStateException()
        val failedResult = Result.failure<List<LaunchDetailsShort>>(exception)
        val repository = mockk<LaunchesRepository>()
        coEvery { repository.getLaunchList() } returns failedResult
        val useCase = LaunchListUseCase(launchesRepository = repository)

        val launchesFlow = useCase.get()

        launchesFlow
            .catch {
                assertThat(it).isEqualTo(exception)
            }
            .collectLatest {}
    }

    @Test
    fun throwExceptionWhenExceptionOccurredInRepository() = runTest {
        val exception = IllegalStateException()
        val repository = mockk<LaunchesRepository>()
        coEvery { repository.getLaunchList() } throws exception
        val useCase = LaunchListUseCase(launchesRepository = repository)

        val launchesFlow = useCase.get()

        launchesFlow
            .catch {
                assertThat(it).isEqualTo(exception)
            }
            .collectLatest {}
    }
}
