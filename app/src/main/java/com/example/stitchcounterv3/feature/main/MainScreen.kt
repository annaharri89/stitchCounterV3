package com.example.stitchcounterv3.feature.main


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stitchcounterv3.R
import com.example.stitchcounterv3.feature.destinations.DoubleCounterScreenDestination
import com.example.stitchcounterv3.feature.destinations.SingleCounterScreenDestination
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.headlineMedium)
            Button(onClick = {
                navigator.navigate(SingleCounterScreenDestination())
            }) {
                Text("New Single Tracker")
            }
            Button(onClick = {
                navigator.navigate(DoubleCounterScreenDestination())
            }) {
                Text("New Double Tracker")
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    StitchCounterV3Theme {
        // Preview without navigation dependencies
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.headlineMedium)
                Button(onClick = {}) {
                    Text("Basic Counter - 1 counter")
                }
                Button(onClick = {}) {
                    Text("Advanced Counter - 2 counters")
                }
            }
        }
    }
}