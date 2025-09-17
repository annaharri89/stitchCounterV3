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
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination
@Composable
fun LibraryScreen(
    navigator: DestinationsNavigator,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val projects by viewModel.projects.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(projects) { project ->
                    ProjectRow(
                        project = project, 
                        onOpen = { viewModel.navigateToEditProject(project, navigator) },
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

