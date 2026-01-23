package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.ui.theme.onQuaternary
import com.example.stitchcounterv3.ui.theme.quaternary

@Composable
fun BottomActionButtons(labelText: String = "Reset",
                        onResetAll: () -> Unit,
                        onSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.quaternary,
                contentColor = MaterialTheme.onQuaternary
            ),
            onClick = { onResetAll.invoke() },
            modifier = Modifier.weight(1f)
        ) {
            Text(labelText)
        }

        Button(
            onClick = { onSave.invoke() },
            modifier = Modifier.weight(1f)
        ) {
            Text("Save")
        }
    }
}