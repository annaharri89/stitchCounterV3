package com.example.stitchcounterv3.feature.doublecounter

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
import com.example.stitchcounterv3.domain.model.CounterState
import com.example.stitchcounterv3.feature.doublecounter.CounterType
import com.example.stitchcounterv3.feature.sharedComposables.BottomActionButtons
import com.example.stitchcounterv3.feature.sharedComposables.CounterView
import com.example.stitchcounterv3.feature.sharedComposables.RowProgressIndicator
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

interface DoubleCounterActions {
    fun setTitle(title: String)
    fun setTotalRows(rows: Int)
    fun increment(type: CounterType)
    fun decrement(type: CounterType)
    fun reset(type: CounterType)
    fun changeAdjustment(type: CounterType, value: AdjustmentAmount)
    fun resetAll()
    fun save()
}

@Composable
fun DoubleCounterPortraitLayout(
    state: DoubleCounterUiState,
    actions: DoubleCounterActions
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
        
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.totalRows.toString(),
            onValueChange = { v -> v.toIntOrNull()?.let(actions::setTotalRows) },
            label = { Text("Total Rows") }
        )

        RowProgressIndicator(
            progress = state.rowProgress
        )

        // Stitches Counter Section
        CounterView(
            modifier = Modifier.weight(1f),
            label = "Stitches",
            count = state.stitchCounterState.count,
            selectedAdjustmentAmount = state.stitchCounterState.adjustment,
            onIncrement = { actions.increment(CounterType.STITCH) },
            onDecrement = { actions.decrement(CounterType.STITCH) },
            onReset = { actions.reset(CounterType.STITCH) },
            onAdjustmentClick = { actions.changeAdjustment(CounterType.STITCH, it) }
        )

        // Rows Counter Section
        CounterView(
            modifier = Modifier.weight(1f),
            label = "Rows/Rounds",
            count = state.rowCounterState.count,
            selectedAdjustmentAmount = state.rowCounterState.adjustment,
            onIncrement = { actions.increment(CounterType.ROW) },
            onDecrement = { actions.decrement(CounterType.ROW) },
            onReset = { actions.reset(CounterType.ROW) },
            onAdjustmentClick = { actions.changeAdjustment(CounterType.ROW, it) }
        )
        
        Spacer(modifier = Modifier.weight(.25f))

        BottomActionButtons(
            onResetAll = actions::resetAll,
            onSave = actions::save
        )
    }
}

@Preview
@Composable
private fun DoubleCounterPortraitPreview() {
    StitchCounterV3Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val fakeActions = object : DoubleCounterActions {
                override fun setTitle(title: String) {}
                override fun setTotalRows(rows: Int) {}
                override fun increment(type: CounterType) {}
                override fun decrement(type: CounterType) {}
                override fun reset(type: CounterType) {}
                override fun changeAdjustment(type: CounterType, value: AdjustmentAmount) {}
                override fun resetAll() {}
                override fun save() {}
            }
            
            DoubleCounterPortraitLayout(
                state = DoubleCounterUiState(
                    title = "Sample Project",
                    stitchCounterState = CounterState(
                        count = 42,
                        adjustment = AdjustmentAmount.FIVE
                    ),
                    rowCounterState = CounterState(
                        count = 10,
                        adjustment = AdjustmentAmount.ONE
                    ),
                    totalRows = 20
                ),
                actions = fakeActions
            )
        }
    }
}