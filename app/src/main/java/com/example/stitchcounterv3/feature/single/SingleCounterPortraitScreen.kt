package com.example.stitchcounterv3.feature.single

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun SingleCounterPortraitScreen(
    state: SingleCounterUiState,
    viewModel: SingleCounterViewModel
) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Basic Counter",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.title,
            onValueChange = { viewModel.setTitle(it) },
            label = { Text("Project Name") }
        )
        Text("Count: ${state.count}", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(
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
                modifier = Modifier.weight(1f).aspectRatio(1f),
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = { viewModel.changeAdjustment(1) }) {
                Text("+1")
            }
            Button(
                colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.tertiary),
                onClick = { viewModel.changeAdjustment(5) }) {
                Text("+5")
            }
            Button(
                colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.error),
                onClick = { viewModel.changeAdjustment(10) }) {
                Text("+10")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { viewModel.save() }) { Text("Save") }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.reset() }) { Text("Reset") }
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