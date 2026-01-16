package com.example.stitchcounterv3.feature.singleCounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.feature.sharedComposables.BottomActionButtons
import com.example.stitchcounterv3.feature.sharedComposables.CounterView
import com.example.stitchcounterv3.feature.sharedComposables.IncreaseDecreaseButtons
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

interface SingleCounterActions {
    fun setTitle(title: String)
    fun increment()
    fun decrement()
    fun resetCount()
    fun changeAdjustment(value: AdjustmentAmount)
    fun save()
}

@Composable
fun SingleCounterPortraitLayout(
    state: SingleCounterUiState,
    actions: SingleCounterActions
) {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = actions::setTitle,
            label = { Text("Project Name") }
        )

        CounterView(
            modifier = Modifier.weight(1f),
            count = state.count,
            selectedAdjustmentAmount = state.adjustment,
            onIncrement = actions::increment,
            onDecrement = actions::decrement,
            onAdjustmentClick = actions::changeAdjustment,
            onReset = actions::resetCount
        )
        
        Spacer(modifier = Modifier.weight(.5f))

        BottomActionButtons(
            onResetAll = actions::resetCount,
            onSave = actions::save
        )
    }
}

@Preview
@Composable
private fun SingleCounterPortraitPreview() {
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
            
            SingleCounterPortraitLayout(
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