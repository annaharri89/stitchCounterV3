package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.domain.model.AdjustmentAmount

@Composable
fun AdjustmentButtons(
    onAdjustmentClick: (AdjustmentAmount) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        AdjustmentAmount.entries.forEach { amount ->
            val buttonColors = when (amount) {
                AdjustmentAmount.ONE -> ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
                AdjustmentAmount.FIVE -> ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
                AdjustmentAmount.TEN -> ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.error
                )
            }

            Button(
                colors = buttonColors,
                onClick = { onAdjustmentClick(amount) }
            ) {
                Text(amount.text)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}