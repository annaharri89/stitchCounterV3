package com.example.stitchcounterv3.feature.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min
import com.example.stitchcounterv3.feature.sharedComposables.AdjustmentButtons
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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Take up available space
            contentAlignment = Alignment.Center
        ) {
            val availableHeight = maxHeight
            val availableWidth = maxWidth
            
            // Calculate font size based on available space
            // Use most of the available space for the text
            val fontSize = min(
                availableHeight.value * 0.8f, // Use 80% of available height
                availableWidth.value * 0.4f // Use 40% of available width
            ).coerceIn(48f, 300f).sp // Clamp between 48sp and 300sp for larger text
            
            Text(
                text = "${state.count}",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = .85f),
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f).aspectRatio(1f),
                onClick = { viewModel.decrement() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { viewModel.increment() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        AdjustmentButtons(
            selectedAdjustmentAmount = state.adjustment,
            onAdjustmentClick = {
                viewModel.changeAdjustment(it)
            })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { viewModel.save() }) { Text("Save") }
        }
        Spacer(modifier = Modifier.weight(.5f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                ),
                onClick = { viewModel.reset() }) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.weight(1f))
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