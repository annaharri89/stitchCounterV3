package com.example.stitchcounterv3.feature.singleCounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.feature.sharedComposables.AdjustmentButtons
import com.example.stitchcounterv3.feature.sharedComposables.BottomActionButtons
import com.example.stitchcounterv3.feature.sharedComposables.IncreaseDecreaseButtons
import com.example.stitchcounterv3.feature.sharedComposables.ResizableText
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun SingleCounterLandscapeLayout(
    state: SingleCounterUiState,
    actions: SingleCounterActions
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
                onValueChange = actions::setTitle,
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

            BottomActionButtons(
                onResetAll = actions::resetCount,
                onSave = actions::save
            )
        }
        
        // Right side - Main counter buttons
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IncreaseDecreaseButtons(
                onIncrement = actions::increment,
                onDecrement = actions::decrement,
                buttonSpacing = 24,
                buttonShape = RoundedCornerShape(12.dp),
                incrementFontSize = 60,
                decrementFontSize = 80
            )

            AdjustmentButtons(
                selectedAdjustmentAmount = state.adjustment,
                onAdjustmentClick = actions::changeAdjustment,
            )
        }
    }
}

@Preview
@Composable
private fun SingleCounterLandscapeScreenPreview() {
    StitchCounterV3Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val fakeActions = object : SingleCounterActions {
                override fun setTitle(title: String) {}
                override fun increment() {}
                override fun decrement() {}
                override fun resetCount() {}
                override fun changeAdjustment(value: AdjustmentAmount) {}
                override fun save() {}
            }
            
            SingleCounterLandscapeLayout(
                state = SingleCounterUiState(
                    title = "Sample Project",
                    count = 42,
                    adjustment = AdjustmentAmount.FIVE
                ),
                actions = fakeActions
            )
        }
    }
}