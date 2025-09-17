package com.example.stitchcounterv3.feature.doublecounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.example.stitchcounterv3.feature.sharedComposables.AdjustmentButtons
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph
@Destination
@Composable
fun DoubleCounterScreen(
    projectId: Int? = null,
    viewModel: DoubleCounterViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(projectId) { viewModel.loadProject(projectId) }

    //todo handle navigation
    
    val state by viewModel.uiState.collectAsState()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = state.title, onValueChange = viewModel::setTitle, label = { Text("Project Name") })
            OutlinedTextField(value = state.totalRows.toString(), onValueChange = { v -> v.toIntOrNull()?.let(viewModel::setTotalRows) }, label = { Text("Total Rows") })

            Text("Stitches: ${state.stitchCount}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = viewModel::decStitch) { Text("-") }
                Button(onClick = viewModel::incStitch) { Text("+") }
                Button(onClick = viewModel::resetStitch) { Text("Reset") }
            }

            AdjustmentButtons(
                selectedAdjustmentAmount = state.stitchAdjustment,
                onAdjustmentClick = {
                    viewModel.changeStitchAdjustment(it)
                })

            Text("Rows: ${state.rowCount}")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = viewModel::decRow) { Text("-") }
                Button(onClick = viewModel::incRow) { Text("+") }
                Button(onClick = viewModel::resetRow) { Text("Reset") }
            }

            AdjustmentButtons(
                selectedAdjustmentAmount = state.rowAdjustment,
                onAdjustmentClick = {
                    viewModel.changeRowAdjustment(it)
                })

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = viewModel::save) { Text("Save") }
                Button(onClick = {
                    viewModel.saveAndGoBack(navigator)
                }) { Text("Save & Back") }
            }
        }
    }
}

@Preview
@Composable
private fun DoublePreview() {
    //StitchCounterV3Theme { DoubleCounterScreen(projectId = null, viewModel = FakeVM()) }
}

private class FakeVM : DoubleCounterViewModel(
    getProject = com.example.stitchcounterv3.domain.usecase.GetProject(
        repo = com.example.stitchcounterv3.data.repo.ProjectRepository(object : com.example.stitchcounterv3.data.local.ProjectDao {
            override fun observeAll() = kotlinx.coroutines.flow.flowOf(emptyList<com.example.stitchcounterv3.data.local.ProjectEntity>())
            override suspend fun getById(id: Int) = null
            override suspend fun upsert(entity: com.example.stitchcounterv3.data.local.ProjectEntity) = 1L
            override suspend fun update(entity: com.example.stitchcounterv3.data.local.ProjectEntity) {}
            override suspend fun delete(entity: com.example.stitchcounterv3.data.local.ProjectEntity) {}
        })
    ),
    upsertProject = com.example.stitchcounterv3.domain.usecase.UpsertProject(
        repo = com.example.stitchcounterv3.data.repo.ProjectRepository(object : com.example.stitchcounterv3.data.local.ProjectDao {
            override fun observeAll() = kotlinx.coroutines.flow.flowOf(emptyList<com.example.stitchcounterv3.data.local.ProjectEntity>())
            override suspend fun getById(id: Int) = null
            override suspend fun upsert(entity: com.example.stitchcounterv3.data.local.ProjectEntity) = 1L
            override suspend fun update(entity: com.example.stitchcounterv3.data.local.ProjectEntity) {}
            override suspend fun delete(entity: com.example.stitchcounterv3.data.local.ProjectEntity) {}
        })
    )
) {
    private val previewState = kotlinx.coroutines.flow.MutableStateFlow(DoubleCounterUiState(title = "Shawl", stitchCount = 10, rowCount = 2, totalRows = 50))
    override val uiState: kotlinx.coroutines.flow.StateFlow<DoubleCounterUiState> get() = previewState
}

