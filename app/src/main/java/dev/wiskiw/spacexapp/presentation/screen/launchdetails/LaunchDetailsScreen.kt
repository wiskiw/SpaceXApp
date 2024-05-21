package dev.wiskiw.spacexapp.presentation.screen.launchdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.wiskiw.spacexapp.R
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsFull
import dev.wiskiw.spacexapp.domain.model.Mission
import dev.wiskiw.spacexapp.domain.model.Rocket
import dev.wiskiw.spacexapp.presentation.compose.ErrorView
import dev.wiskiw.spacexapp.presentation.compose.ProgressView
import dev.wiskiw.spacexapp.presentation.theme.SpaceXAppTheme
import dev.wiskiw.spacexapp.presentation.theme.size
import dev.wiskiw.spacexapp.presentation.tool.mvi.ConsumeSideEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun LaunchDetailsScreen(
    viewModel: LaunchDetailsViewModel = koinViewModel(),
    launchId: String,
    navigateBack: () -> Unit,
) {
    ConsumeSideEffect(viewModel = viewModel) { sideEffect ->
        when (sideEffect) {
            is LaunchDetailsViewModel.SideEffect.NavigateBack -> {
                navigateBack()
            }
        }
    }

    LaunchedEffect(launchId) {
        viewModel.onArgsReceived(
            launchId = launchId,
        )
    }

    Content(
        state = viewModel.uiState,
        onAction = viewModel::handleAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    state: LaunchDetailsUiState,
    onAction: (LaunchDetailsViewModel.Action) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    title = {
                        Text(
                            stringResource(
                                id = R.string.screen_launch_details_title,
                                state.launchId,
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onAction(LaunchDetailsViewModel.Action.OnBackClick)
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate back")
                        }
                    },
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
                    onRetry = { onAction(LaunchDetailsViewModel.Action.OnRetryClick) },
                )

                state.details != null -> LaunchDetails(
                    modifier = Modifier.padding(scaffoldPaddings),
                    launchDetails = state.details
                )
            }
        }
    }
}

@Composable
private fun LaunchDetails(
    modifier: Modifier = Modifier,
    launchDetails: LaunchDetailsFull,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(MaterialTheme.size.two)
            .verticalScroll(scrollState),
    ) {
        AsyncImage(
            modifier = Modifier
                .aspectRatio(1f),
            model = ImageRequest.Builder(LocalContext.current)
                .data(launchDetails.mission.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(
                id = R.string.screen_launch_details_mission_image_content_description,
                launchDetails.id,
            ),
        )
        Spacer(Modifier.height(MaterialTheme.size.two))

        RocketDetails(rocket = launchDetails.rocket)
        Spacer(Modifier.height(MaterialTheme.size.two))

        MissionDetails(mission = launchDetails.mission)
        Spacer(Modifier.height(MaterialTheme.size.two))

        SiteDetails(site = launchDetails.site)
        Spacer(Modifier.height(MaterialTheme.size.two))
    }
}

@Composable
private fun RocketDetails(
    modifier: Modifier = Modifier,
    rocket: Rocket,
) {
    Column(
        modifier = modifier,
    ) {
        DetailsTitle(
            text = stringResource(id = R.string.screen_launch_details_rocket_title),
        )

        DetailsProperty(
            text = stringResource(
                id = R.string.screen_launch_details_rocket_name_label,
                rocket.name,
            ),
        )

        DetailsProperty(
            text = stringResource(
                id = R.string.screen_launch_details_rocket_type_label,
                rocket.type,
            ),
        )

        DetailsProperty(
            text = stringResource(
                id = R.string.screen_launch_details_rocket_id_label,
                rocket.id,
            ),
        )
    }
}

@Composable
private fun MissionDetails(
    modifier: Modifier = Modifier,
    mission: Mission,
) {
    Column(
        modifier = modifier,
    ) {
        DetailsTitle(
            text = stringResource(id = R.string.screen_launch_details_mission_title),
        )
        DetailsProperty(
            text = stringResource(
                id = R.string.screen_launch_details_mission_name_label,
                mission.name,
            ),
        )
    }
}

@Composable
private fun SiteDetails(
    modifier: Modifier = Modifier,
    site: String,
) {
    Column(
        modifier = modifier,
    ) {
        DetailsTitle(
            text = stringResource(id = R.string.screen_launch_details_site_title),
        )
        DetailsProperty(text = site)
    }
}

@Composable
private fun DetailsTitle(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun DetailsProperty(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.onSecondary,
        style = MaterialTheme.typography.bodyMedium,
    )
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    SpaceXAppTheme(
        darkTheme = false,
    ) {
        val launchId = "123"
        val launchDetails = LaunchDetailsFull(
            id = launchId,
            rocket = Rocket(
                id = "RockerId",
                name = "Falcon Heavy",
                type = "SFK-2",
            ),
            mission = Mission(
                name = "StarLink",
                imageUrl = "https://images2.imgbox.com/b5/1d/46Eo0yuu_o.png",
            ),
            site = "CCAFS SLC 40",
        )
        val state = LaunchDetailsUiState(
            launchId = launchId,
            isLoading = false,
            error = null,
            details = launchDetails,
        )
        Content(
            state = state,
            onAction = {}
        )
    }
}
