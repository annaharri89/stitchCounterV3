package com.example.stitchcounterv3.feature.doublecounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.feature.sharedComposables.BottomActionButtons
import com.example.stitchcounterv3.feature.sharedComposables.CounterView
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun DoubleCounterPortraitLayout(
    state: DoubleCounterUiState,
    viewModel: DoubleCounterViewModel,
    onResetAll: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = { viewModel.setTitle(it) },
            label = { Text("Project Name") }
        )
        
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.totalRows.toString(),
            onValueChange = { v -> v.toIntOrNull()?.let(viewModel::setTotalRows) },
            label = { Text("Total Rows") }
        )

        // Stitches Counter Section
        CounterView(
            modifier = Modifier.weight(1f),
            label = "Stitches",
            count = state.stitchCount,
            selectedAdjustmentAmount = state.stitchAdjustment,
            onIncrement = { viewModel.incStitch() },
            onDecrement = { viewModel.decStitch() },
            onReset = { viewModel.resetStitch() },
            onAdjustmentClick = { viewModel.changeStitchAdjustment(it) }
        )

        // Rows Counter Section
        CounterView(
            modifier = Modifier.weight(1f),
            label = "Rows/Rounds",
            count = state.rowCount,
            selectedAdjustmentAmount = state.rowAdjustment,
            onIncrement = { viewModel.incRow() },
            onDecrement = { viewModel.decRow() },
            onReset = { viewModel.resetRow() },
            onAdjustmentClick = { viewModel.changeRowAdjustment(it) }
        )
        
        Spacer(modifier = Modifier.weight(.25f))

        BottomActionButtons(
            onResetAll = onResetAll,
            onSave = onSave
        )
    }
}

@Preview
@Composable
private fun DoubleCounterPortraitPreview() {
    StitchCounterV3Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DoubleCounterPortraitLayout(
                DoubleCounterUiState(), viewModel(), {}, { }
            )
        }
    }
}