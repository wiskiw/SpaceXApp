package dev.wiskiw.spacexapp.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.usecase.LaunchListUseCase
import dev.wiskiw.spacexapp.presentation.screen.launches.LaunchesViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LaunchesViewModelTest {

    private fun createViewModel(
        savedStateHandle: SavedStateHandle = mockk<SavedStateHandle>(relaxed = true) {
            every { this@mockk.get<Any>(any()) } returns null
        },
        launchListUseCase: LaunchListUseCase = mockk<LaunchListUseCase>(relaxed = true),
    ) = LaunchesViewModel(
        savedStateHandle = savedStateHandle,
        launchListUseCase = launchListUseCase,
    )

    @Test
    fun uiStateLoadingIsTrueWhenCreated() = runTest {
        val launchListUseCase = mockk<LaunchListUseCase>(relaxed = true)

        val viewModel = createViewModel(
            launchListUseCase = launchListUseCase,
        )

        viewModel.uiStateFlow.test {
            Truth.assertThat(expectMostRecentItem().isLoading).isTrue()
        }
    }

    @Test
    fun fetchesDataFromUseCaseWhenWhenCreated() = runTest {
        val launchListUseCase = mockk<LaunchListUseCase>(relaxed = true)

        createViewModel(
            launchListUseCase = launchListUseCase,
        )

        verify(exactly = 1) { launchListUseCase.get() }
    }

    @Test
    fun fetchesDataFromUseCaseWhenLaunchIdExistAndRetryClicked() {
        val launchListUseCase = mockk<LaunchListUseCase>(relaxed = true)
        val viewModel = createViewModel(
            launchListUseCase = launchListUseCase,
        )

        viewModel.handleAction(LaunchesViewModel.Action.OnRetryClick)

        // 1st triggered by initial fetching
        // 2nd triggered by OnRetryClick
        verify(exactly = 2) { launchListUseCase.get() }
    }

    @Test
    fun emitsLoadingTrueWhenRetryClicked() = runTest {
        val launchListUseCase = mockk<LaunchListUseCase>(relaxed = true) {
            every { this@mockk.get() } returns flowOf(emptyList<LaunchDetailsShort>())
        }
        val viewModel = createViewModel(
            launchListUseCase = launchListUseCase,
        )
        viewModel.uiStateFlow.test {
            // preparing ui state
            awaitItem() // skipping loading=false caused by initial fetching

            // act
            viewModel.handleAction(LaunchesViewModel.Action.OnRetryClick)

            // assert
            Truth.assertThat(awaitItem().isLoading).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun emitsErrorWhenLaunchDetailsUseCaseThrowsException() = runTest {
        val launchListUseCase = mockk<LaunchListUseCase>(relaxed = true) {
            every { this@mockk.get() } returns flow { throw Exception("Any Exception") }
        }
        val viewModel = createViewModel(
            launchListUseCase = launchListUseCase,
        )

        viewModel.uiStateFlow.test {
            val stateWithError = awaitItem()
            Truth.assertThat(stateWithError.isLoading).isFalse()
            Truth.assertThat(stateWithError.error).isNotNull()
            cancelAndIgnoreRemainingEvents()
        }
    }
}
