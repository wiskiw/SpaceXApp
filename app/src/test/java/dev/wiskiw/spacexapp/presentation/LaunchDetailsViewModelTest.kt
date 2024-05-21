package dev.wiskiw.spacexapp.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.wiskiw.spacexapp.app.logger.AppLogger
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.usecase.LaunchDetailsUseCase
import dev.wiskiw.spacexapp.presentation.screen.launchdetails.LaunchDetailsViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class LaunchDetailsViewModelTest {

    private fun createViewModel(
        savedStateHandle: SavedStateHandle = mockk<SavedStateHandle>(relaxed = true) {
            every { this@mockk.get<Any>(any()) } returns null
        },
        detailsUseCase: LaunchDetailsUseCase = mockk<LaunchDetailsUseCase>(relaxed = true),
        logger: AppLogger = mockk<AppLogger>(relaxed = true),
    ) = LaunchDetailsViewModel(
        savedStateHandle = savedStateHandle,
        detailsUseCase = detailsUseCase,
        logger = logger,
    )

    @Test
    fun uiStateLoadingIsTrueWhenCreated() = runTest {
        val detailsUseCase = mockk<LaunchDetailsUseCase>(relaxed = true)
        val logger = mockk<AppLogger>(relaxed = true)

        val viewModel = createViewModel(
            detailsUseCase = detailsUseCase,
            logger = logger,
        )

        viewModel.uiStateFlow.test {
            assertThat(expectMostRecentItem().isLoading).isTrue()
        }
    }

    @Test
    fun fetchesDataFromUseCaseWhenLaunchIdNavArgumentReceived() {
        val detailsUseCase = mockk<LaunchDetailsUseCase>(relaxed = true)
        val logger = mockk<AppLogger>(relaxed = true)
        val viewModel = createViewModel(
            detailsUseCase = detailsUseCase,
            logger = logger,
        )
        val launchId = "launchId"

        viewModel.onArgsReceived(launchId)

        verify(exactly = 1) { detailsUseCase.get(eq(launchId)) }
    }

    @Test
    fun fetchesDataFromUseCaseWhenLaunchIdNavArgumentChanged() {
        val detailsUseCase = mockk<LaunchDetailsUseCase>(relaxed = true)
        val logger = mockk<AppLogger>(relaxed = true)
        val viewModel = createViewModel(
            detailsUseCase = detailsUseCase,
            logger = logger,
        )

        val originalLaunchId = "originalLaunchId"
        viewModel.onArgsReceived(originalLaunchId)

        val newLaunchId = "newLaunchId"
        viewModel.onArgsReceived(newLaunchId)

        verifySequence {
            detailsUseCase.get(eq(originalLaunchId))
            detailsUseCase.get(eq(newLaunchId))
        }
    }

    @Test
    fun skipsFetchingDataFromUseCaseWhenLaunchIdNavArgumentNotChanged() {
        val detailsUseCase = mockk<LaunchDetailsUseCase>(relaxed = true)
        val logger = mockk<AppLogger>(relaxed = true)
        val viewModel = createViewModel(
            detailsUseCase = detailsUseCase,
            logger = logger,
        )
        val launchId = "launchId "
        viewModel.onArgsReceived(launchId)
        viewModel.onArgsReceived(launchId)

        verify(exactly = 1) { detailsUseCase.get(eq(launchId)) }
    }

    @Test
    fun fetchesDataFromUseCaseWhenLaunchIdExistAndRetryClicked() {
        val detailsUseCase = mockk<LaunchDetailsUseCase>(relaxed = true)
        val logger = mockk<AppLogger>(relaxed = true)
        val viewModel = createViewModel(
            detailsUseCase = detailsUseCase,
            logger = logger,
        )
        val launchId = "launchId"
        viewModel.onArgsReceived(launchId)

        viewModel.handleAction(LaunchDetailsViewModel.Action.OnRetryClick)

        // 1st triggered by onArgsReceived
        // 2nd triggered by OnRetryClick
        verify(exactly = 2) { detailsUseCase.get(launchId) }
    }

    @Test
    fun emitsLoadingTrueWhenRetryClicked() = runTest {
        val detailsUseCase = mockk<LaunchDetailsUseCase>(relaxed = true) {
            every { this@mockk.get(any()) } returns flowOf(mockk<LaunchDetailsFull>())
        }
        val logger = mockk<AppLogger>(relaxed = true)
        val viewModel = createViewModel(
            detailsUseCase = detailsUseCase,
            logger = logger,
        )
        viewModel.uiStateFlow.test {
            // preparing ui state
            viewModel.onArgsReceived("launchId")
            awaitItem() // skipping update of launchId value caused by onArgsReceived
            awaitItem() // skipping loading=true caused by onArgsReceived
            awaitItem() // skipping loading=false caused by onArgsReceived

            // act
            viewModel.handleAction(LaunchDetailsViewModel.Action.OnRetryClick)

            // assert
            assertThat(awaitItem().isLoading).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun emitsErrorWhenLaunchDetailsUseCaseThrowsException() = runTest {
        val detailsUseCase = mockk<LaunchDetailsUseCase> {
            every { this@mockk.get(any()) } returns flow { throw Exception("Any Exception") }
        }
        val logger = mockk<AppLogger>(relaxed = true)
        val viewModel = createViewModel(
            detailsUseCase = detailsUseCase,
            logger = logger,
        )

        viewModel.uiStateFlow.test {
            viewModel.onArgsReceived("launchId")
            awaitItem() // skipping update of launchId value caused by onArgsReceived
            awaitItem() // skipping loading=true caused by onArgsReceived

            val stateWithError = awaitItem()
            assertThat(stateWithError.isLoading).isFalse()
            assertThat(stateWithError.error).isNotNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
