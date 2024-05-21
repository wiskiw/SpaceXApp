package dev.wiskiw.spacexapp.presentation.screen.launches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.wiskiw.spacexapp.R
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.model.Mission
import dev.wiskiw.spacexapp.presentation.compose.ErrorView
import dev.wiskiw.spacexapp.presentation.compose.ProgressView
import dev.wiskiw.spacexapp.presentation.theme.SpaceXAppTheme
import dev.wiskiw.spacexapp.presentation.theme.size
import dev.wiskiw.spacexapp.presentation.tool.NavDestination
import dev.wiskiw.spacexapp.presentation.tool.mvi.ConsumeSideEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun LaunchesScreen(
    viewModel: LaunchesViewModel = koinViewModel(),
    navigateTo: (NavDestination) -> Unit,
) {

    ConsumeSideEffect(viewModel = viewModel) { sideEffect ->
        when (sideEffect) {
            is LaunchesViewModel.SideEffect.NavigateToLaunchDetails -> {
                val destination = NavDestination.LaunchDetails(
                    launchId = sideEffect.launchId,
                )
                navigateTo(destination)
            }
        }
    }

    Content(
        state = viewModel.uiState,
        onAction = viewModel::handleAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: LaunchesUiState,
    onAction: (LaunchesViewModel.Action) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        Scaffold(
            topBar = {
                MediumTopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    title = {
                        Text(stringResource(id = R.string.screen_launches_title))
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { scaffoldPaddings ->
            when {
                state.isLoading -> ProgressView(
                    modifier = Modifier.padding(scaffoldPaddings),
                )

                state.error != null -> ErrorView(
                    modifier = Modifier.padding(scaffoldPaddings),
                    text = state.error,
                    onRetry = { onAction(LaunchesViewModel.Action.OnRetryClick) },
                )

                else -> LaunchList(
                    modifier = Modifier.padding(scaffoldPaddings),
                    scrollConnection = scrollBehavior.nestedScrollConnection,
                    launches = state.launches
                ) { launchId ->
                    val action = LaunchesViewModel.Action.OnLaunchClick(launchId)
                    onAction(action)
                }
            }
        }
    }
}

@Composable
private fun LaunchList(
    modifier: Modifier = Modifier,
    scrollConnection: NestedScrollConnection,
    launches: List<LaunchDetailsShort>,
    onClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.nestedScroll(scrollConnection),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.one)
    ) {
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.size.three))
        }
        items(launches.size) {
            val launch = launches[it]
            LaunchItem(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.size.one)
                    .fillMaxWidth(),
                launchDetails = launch,
                onClick = {
                    onClick(launch.id)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.size.three))
        }
    }
}

@Composable
private fun LaunchItem(
    modifier: Modifier = Modifier,
    launchDetails: LaunchDetailsShort,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(MaterialTheme.size.one)
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = shape,
            )
            .clip(shape)
            .clickable { onClick.invoke() }
            .padding(MaterialTheme.size.one),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.size.one),
    ) {
        AsyncImage(
            modifier = Modifier
                .size(MaterialTheme.size.six),
            model = ImageRequest.Builder(LocalContext.current)
                .data(launchDetails.mission.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(
                id = R.string.screen_launches_launch_item_image_content_description,
                launchDetails.mission.name,
            ),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.half),
        ) {
            Text(
                text = launchDetails.rocketName,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = launchDetails.mission.name,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    val launches = listOf(
        LaunchDetailsShort(
            id = "110",
            rocketName = "Falcon 9",
            mission = Mission(
                name = "Starlink-15",
                imageUrl = "https://imgur.com/E7fjUBD.png"
            )
        ),
        LaunchDetailsShort(
            id = "9",
            rocketName = "Falcon Heavy",
            mission = Mission(
                name = "Tesla Plaid",
                imageUrl = "https://images2.imgbox.com/d2/3b/bQaWiil0_o.png"
            )
        )
    )

    SpaceXAppTheme(
        darkTheme = false,
    ) {
        Content(
            state = LaunchesUiState(
                isLoading = false,
                error = null,
                launches = launches,
            ),
            onAction = {}
        )
    }
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun LaunchItemPreviewDark() {
    val launchDetails = LaunchDetailsShort(
        id = "110",
        rocketName = "Falcon 9",
        mission = Mission(
            name = "Starlink-15",
            imageUrl = "https://imgur.com/E7fjUBD.png"
        )
    )

    SpaceXAppTheme(
        darkTheme = true,
    ) {
        LaunchItem(
            launchDetails = launchDetails,
        )
    }
}
