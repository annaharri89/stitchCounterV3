package com.example.stitchcounterv3.feature.doublecounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.feature.sharedComposables.CounterView
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun DoubleCounterLandscapeLayout(
    state: DoubleCounterUiState,
    viewModel: DoubleCounterViewModel
) {
    Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Left side - Project info and Stitches counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.setTitle(it) },
                label = { Text("Project Name") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = state.totalRows.toString(),
                onValueChange = { v -> v.toIntOrNull()?.let(viewModel::setTotalRows) },
                label = { Text("Total Rows") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Left side - Stitch counter
            CounterView(
                modifier = Modifier.weight(1f),
                label = "Stitches",
                count = state.stitchCount,
                selectedAdjustmentAmount = state.stitchAdjustment,
                onIncrement = { viewModel.incStitch() },
                onDecrement = { viewModel.decStitch() },
                onAdjustmentClick = { viewModel.changeStitchAdjustment(it) },
                onReset = {}
            )

            // Right side - Rows counter
            CounterView(
                modifier = Modifier.weight(1f),
                label = "Rows/Rounds",
                count = state.rowCount,
                selectedAdjustmentAmount = state.rowAdjustment,
                onIncrement = { viewModel.incRow() },
                onDecrement = { viewModel.decRow() },
                onAdjustmentClick = { viewModel.changeRowAdjustment(it) },
                onReset = {

                }
            )
        }

        // Bottom action buttons - positioned at the bottom of the screen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.save() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                ),
                onClick = { viewModel.resetStitch(); viewModel.resetRow() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset All")
            }
        }
    }
}

@Preview
@Composable
private fun DoubleCounterLandscapePreview() {
    StitchCounterV3Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Left side - Project info and Stitches counter
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = "Sample Project",
                        onValueChange = { },
                        label = { Text("Project Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = "50",
                        onValueChange = { },
                        label = { Text("Total Rows") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                CounterView(
                    modifier = Modifier.weight(1f),
                    label = "Stitches",
                    count = 42,
                    selectedAdjustmentAmount = AdjustmentAmount.FIVE,
                    onIncrement = { },
                    onDecrement = { },
                    onAdjustmentClick = { },
                    onReset = {

                    }
                )

                // Right side - Rows counter
                CounterView(
                    modifier = Modifier.weight(1f),
                    label = "Rows/Rounds",
                    count = 15,
                    selectedAdjustmentAmount = AdjustmentAmount.FIVE,
                    onIncrement = { },
                    onDecrement = { },
                    onAdjustmentClick = { },
                    onReset = {

                    }
                )
            }
            
            // Bottom action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { }, modifier = Modifier.weight(1f)) { Text("Save") }
                Button(onClick = { }, modifier = Modifier.weight(1f)) { Text("Reset All") }
            }
        }
    }
}