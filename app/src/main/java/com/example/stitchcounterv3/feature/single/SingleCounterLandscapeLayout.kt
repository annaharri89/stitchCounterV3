package com.example.stitchcounterv3.feature.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.feature.sharedComposables.AdjustmentButtons
import com.example.stitchcounterv3.feature.sharedComposables.CounterButtons
import com.example.stitchcounterv3.feature.sharedComposables.ResizableText
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun SingleCounterLandscapeLayout(
    state: SingleCounterUiState,
    viewModel: SingleCounterViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left side - Project info and controls
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.setTitle(it) },
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth()
            )

            ResizableText(
                text = state.count.toString(),
                modifier = Modifier.weight(1f),
                heightRatio =  0.8f,
                widthRatio = 0.4f,
                minFontSize = 48f,
                maxFontSize = 300f
            )

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ),
                    onClick = { viewModel.resetCount() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = { viewModel.save() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
        
        // Right side - Main counter buttons
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CounterButtons(
                onIncrement = { viewModel.increment() },
                onDecrement = { viewModel.decrement() },
                buttonSpacing = 24,
                buttonShape = RoundedCornerShape(12.dp),
                incrementFontSize = 60,
                decrementFontSize = 80
            )

            AdjustmentButtons(
                selectedAdjustmentAmount = state.adjustment,
                onAdjustmentClick = { viewModel.changeAdjustment(it) },
            )
        }
    }
}

@Preview
@Composable
private fun SingleCounterLandscapeScreenPreview() {
    StitchCounterV3Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Preview the layout directly without ViewModel dependency
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Left side - Project info and controls
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = "Sample Project",
                        onValueChange = { },
                        label = { Text("Project Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) { 
                            Text("Save") 
                        }
                        
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = androidx.compose.ui.graphics.Color.White
                            ),
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }
        }
    }
}