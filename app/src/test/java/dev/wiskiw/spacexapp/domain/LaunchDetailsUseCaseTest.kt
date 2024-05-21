package dev.wiskiw.spacexapp.domain

import com.google.common.truth.Truth.assertThat
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.repository.LaunchesRepository
import dev.wiskiw.spacexapp.domain.usecase.LaunchDetailsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LaunchDetailsUseCaseTest {

    @Test
    fun emitsValueFromResultWhenRepositoryReturnsResultSuccess() = runTest {
        val launchId = "launchId"
        val details = mockk<LaunchDetailsFull>()
        val successResult = Result.success(details)
        val repository = mockk<LaunchesRepository>()
        coEvery { repository.getLaunchDetails(eq(launchId)) } returns successResult
        val useCase = LaunchDetailsUseCase(launchesRepository = repository)

        val detailsFlow = useCase.get(launchId)

        detailsFlow.collectLatest {
            assertThat(it).isEqualTo(details)
        }
    }

    @Test
    fun throwExceptionWhenRepositoryReturnsResultFailed() = runTest {
        val launchId = "launchId"
        val exception = IllegalStateException()
        val failedResult = Result.failure<LaunchDetailsFull>(exception)
        val repository = mockk<LaunchesRepository>()
        coEvery { repository.getLaunchDetails(eq(launchId)) } returns failedResult
        val useCase = LaunchDetailsUseCase(launchesRepository = repository)

        val detailsFlow = useCase.get(launchId)

        detailsFlow
            .catch {
                assertThat(it).isEqualTo(exception)
            }
            .collectLatest {}
    }

    @Test
    fun throwExceptionWhenExceptionOccurredInRepository() = runTest {
        val launchId = "launchId"
        val exception = IllegalStateException()
        val repository = mockk<LaunchesRepository>()
        coEvery { repository.getLaunchDetails(eq(launchId)) } throws exception
        val useCase = LaunchDetailsUseCase(launchesRepository = repository)

        val detailsFlow = useCase.get(launchId)

        detailsFlow
            .catch {
                assertThat(it).isEqualTo(exception)
            }
            .collectLatest {}
    }
}
