package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.domain.model.AdjustmentAmount

@Composable
fun CounterSection(
    label: String,
    count: Int,
    selectedAdjustmentAmount: AdjustmentAmount,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onAdjustmentClick: (AdjustmentAmount) -> Unit,
    modifier: Modifier = Modifier,
    textWeight: Float = 0.5f,
    buttonsWeight: Float = 1f,
    textPaddingEnd: Float = 24f,
    buttonSpacing: Int = 24,
    buttonShape: RoundedCornerShape = RoundedCornerShape(12.dp),
    incrementFontSize: Int = 50,
    decrementFontSize: Int = 60
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ResizableText(
                modifier = Modifier
                    .weight(textWeight)
                    .padding(end = textPaddingEnd.dp),
                text = "$count",
                heightRatio = 0.6f,
                widthRatio = 0.3f,
                minFontSize = 48f,
                maxFontSize = 200f
            )

            CounterButtons(
                modifier = Modifier.weight(buttonsWeight),
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                buttonSpacing = buttonSpacing,
                buttonShape = buttonShape,
                incrementFontSize = incrementFontSize,
                decrementFontSize = decrementFontSize
            )
        }

        Row {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                ),
                onClick = { onReset() },
                modifier = Modifier.weight(textWeight)
            ) {
                Text("Reset")
            }

            AdjustmentButtons(
                selectedAdjustmentAmount = selectedAdjustmentAmount,
                onAdjustmentClick = onAdjustmentClick,
                modifier = Modifier.weight(buttonsWeight)
            )
        }
    }
}