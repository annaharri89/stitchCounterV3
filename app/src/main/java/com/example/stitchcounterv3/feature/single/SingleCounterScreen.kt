package com.example.stitchcounterv3.feature.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.stitchcounterv3.domain.model.NavigationEvent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stitchcounterv3.feature.navigation.BottomNavGraph
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack

@BottomNavGraph
@Destination
@Composable
fun SingleCounterScreen(
    projectId: Int? = null,
    viewModel: SingleCounterViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(projectId) {
        viewModel.load(projectId)
    }
    
    // Collect navigation events and handle them
    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToScreen -> {
                    navigator.navigate(event.destination)
                }
                is NavigationEvent.PopBackStack -> {
                    navigator.popBackStack()
                }
                is NavigationEvent.NavigateUp -> {
                    navigator.popBackStack()
                }
            }
        }
    }
    
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Basic Counter", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.setTitle(it) },
                label = { Text("Project Name") }
            )
            Text("Count: ${state.count}", style = MaterialTheme.typography.headlineMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { viewModel.decrement() }) { Text("-") }
                Button(onClick = { viewModel.increment() }) { Text("+") }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = { viewModel.reset() }) { Text("Reset") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { viewModel.changeAdjustment(1) }) { Text("+1") }
                Button(onClick = { viewModel.changeAdjustment(5) }) { Text("+5") }
                Button(onClick = { viewModel.changeAdjustment(10) }) { Text("+10") }
            }
            Button(onClick = { viewModel.save() }) { Text("Save") }
            Button(onClick = { viewModel.goToLibrary() }) { Text("Go to Library") }
        }
    }
}

@Preview
@Composable
private fun SingleCounterPreview() {
    StitchCounterV3Theme {
        // Preview without navigation dependencies
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Basic Counter", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = "Sample Project",
                    onValueChange = { },
                    label = { Text("Project Name") }
                )
                Text("Count: 0", style = MaterialTheme.typography.headlineMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { }) { Text("-") }
                    Button(onClick = { }) { Text("+") }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { }) { Text("Reset") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { }) { Text("+1") }
                    Button(onClick = { }) { Text("+5") }
                    Button(onClick = { }) { Text("+10") }
                }
                Button(onClick = { }) { Text("Save") }
                Button(onClick = { }) { Text("Go to Library") }
            }
        }
    }
}