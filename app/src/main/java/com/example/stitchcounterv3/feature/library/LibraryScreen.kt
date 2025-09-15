package com.example.stitchcounterv3.feature.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stitchcounterv3.domain.model.NavigationEvent
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.feature.navigation.BottomNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack

@BottomNavGraph
@Destination
@Composable
fun LibraryScreen(
    navigator: DestinationsNavigator,
    viewModel: LibraryViewModel = hiltViewModel()
) {
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

    val projects by viewModel.projects.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Add buttons to create new projects
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.navigateToNewSingleCounter() }) {
                    Text("New Basic Counter")
                }
                Button(onClick = { viewModel.navigateToNewDoubleCounter() }) {
                    Text("New Advanced Counter")
                }
            }
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(projects) { project ->
                    ProjectRow(
                        project = project, 
                        onOpen = { viewModel.navigateToEditProject(project) }, 
                        onDelete = { viewModel.delete(project) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun ProjectRow(project: Project, onOpen: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(project.title.ifBlank { "Untitled" })
            Text("Type: ${'$'}{project.type}")
        }
        Button(onClick = onDelete) { Text("Delete") }
    }
}

