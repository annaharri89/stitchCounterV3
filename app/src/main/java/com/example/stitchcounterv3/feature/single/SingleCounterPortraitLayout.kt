package com.example.stitchcounterv3.feature.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.feature.sharedComposables.AdjustmentButtons
import com.example.stitchcounterv3.feature.sharedComposables.CounterButtons
import com.example.stitchcounterv3.feature.sharedComposables.ResizableText
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun SingleCounterPortraitLayout(
    state: SingleCounterUiState,
    viewModel: SingleCounterViewModel
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

        ResizableText(
            text = state.count.toString(),
            modifier = Modifier.weight(1f),
            heightRatio =  0.8f,
            widthRatio = 0.4f,
            minFontSize = 48f,
            maxFontSize = 300f
        )

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
        
        Spacer(modifier = Modifier.weight(.5f))

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
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
}

@Preview
@Composable
private fun SingleCounterPortraitPreview() {
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
                CounterButtons(
                    onIncrement = { },
                    onDecrement = { },
                    buttonSpacing = 24,
                    buttonShape = RoundedCornerShape(12.dp),
                    incrementFontSize = 60,
                    decrementFontSize = 80
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { }) { Text("Reset") }
                    Spacer(modifier = Modifier.weight(1f))
                }
                Button(onClick = { }) { Text("Save") }
                Button(onClick = { }) { Text("Go to Library") }
            }
        }
    }
}