package dev.wiskiw.spacexapp.presentation.screen.launches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.wiskiw.spacexapp.domain.model.LaunchDetailsShort
import dev.wiskiw.spacexapp.domain.model.Mission
import dev.wiskiw.spacexapp.presentation.theme.size

@Composable
fun LaunchesScreen(
    // todo inject VM
    viewModel: LaunchesViewModel,
) {
    Content(
        state = viewModel.uiState,
        onAction = viewModel::handleAction,
    )
}

@Composable
private fun Content(
    state: LaunchesUiState,
    onAction: (LaunchesViewModel.Action) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn {
            items(state.launches.size) {
                val launch = state.launches[it]
                LaunchItem(
                    modifier = Modifier.fillMaxWidth(),
                    launchDetails = launch,
                    onClick = {
                        val action = LaunchesViewModel.Action.OnLaunchClick(launch.id)
                        onAction(action)
                    }
                )
            }
        }
    }
}

@Composable
private fun LaunchItem(
    modifier: Modifier = Modifier,
    launchDetails: LaunchDetailsShort,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .padding(MaterialTheme.size.one),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(MaterialTheme.size.six)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.size.one))
        Column(
            modifier = Modifier,
        ) {
            Text(text = launchDetails.rocketName)
            Text(text = launchDetails.mission.name)
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreview() {
    val launches = listOf(
        LaunchDetailsShort(
            id = "110",
            rocketName = "Falcon 9",
            mission = Mission(
                name = "Starlink-15",
                imageUrl = "todo"
            )
        ),
        LaunchDetailsShort(
            id = "9",
            rocketName = "Falcon Heavy",
            mission = Mission(
                name = "Tesla Plaid",
                imageUrl = "todo"
            )
        )
    )

    Content(
        state = LaunchesUiState(
            launches = launches,
        ),
        onAction = {}
    )
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun LaunchItemPreview() {
    val launchDetails = LaunchDetailsShort(
        id = "110",
        rocketName = "Falcon 9",
        mission = Mission(
            name = "Starlink-15",
            imageUrl = "todo"
        )
    )

    LaunchItem(
        launchDetails = launchDetails,
    )
}
