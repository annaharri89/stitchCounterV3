package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.domain.model.AdjustmentAmount

@Composable
fun CounterSectionLandscape(
    label: String,
    count: Int,
    selectedAdjustmentAmount: AdjustmentAmount,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onAdjustmentClick: (AdjustmentAmount) -> Unit,
    modifier: Modifier = Modifier,
    textWeight: Float = 1f,
    buttonSpacing: Int = 12,
    buttonShape: RoundedCornerShape = RoundedCornerShape(12.dp),
    incrementFontSize: Int = 50,
    decrementFontSize: Int = 60,
    showTopSpacer: Boolean = false,
    showBottomSpacer: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        if (showTopSpacer) {
            Spacer(modifier = Modifier.weight(0.5f))
        }
        
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        ResizableText(
            text = "$count",
            modifier = Modifier.weight(textWeight),
            heightRatio = 0.6f,
            widthRatio = 0.4f,
            minFontSize = 48f,
            maxFontSize = 200f
        )

        IncreaseDecreaseButtons(
            onIncrement = onIncrement,
            onDecrement = onDecrement,
            buttonSpacing = buttonSpacing,
            buttonShape = buttonShape,
            incrementFontSize = incrementFontSize,
            decrementFontSize = decrementFontSize
        )
        
        if (showBottomSpacer) {
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Preview
@Composable
fun CounterSectionLandscapePreview() {
    CounterSectionLandscape(count = 5, onDecrement = {}, onIncrement = {}, label = "Stitches", onAdjustmentClick = {}, selectedAdjustmentAmount = AdjustmentAmount.FIVE)
}
