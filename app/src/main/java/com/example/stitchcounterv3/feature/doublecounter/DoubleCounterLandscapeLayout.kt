package com.example.stitchcounterv3.feature.doublecounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.feature.sharedComposables.BottomActionButtons
import com.example.stitchcounterv3.feature.sharedComposables.CounterView
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun DoubleCounterLandscapeLayout(
    state: DoubleCounterUiState,
    actions: DoubleCounterActions
) {
    Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Left side - Project info and Stitches counter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = actions::setTitle,
                label = { Text("Project Name") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = state.totalRows.toString(),
                onValueChange = { v -> v.toIntOrNull()?.let(actions::setTotalRows) },
                label = { Text("Total Rows") },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Left side - Stitch counter
            CounterView(
                modifier = Modifier.weight(1f),
                label = "Stitches",
                count = state.stitchCount,
                selectedAdjustmentAmount = state.stitchAdjustment,
                onIncrement = actions::incStitch,
                onDecrement = actions::decStitch,
                onAdjustmentClick = actions::changeStitchAdjustment,
                onReset = actions::resetStitch
            )

            // Right side - Rows counter
            CounterView(
                modifier = Modifier.weight(1f),
                label = "Rows/Rounds",
                count = state.rowCount,
                selectedAdjustmentAmount = state.rowAdjustment,
                onIncrement = actions::incRow,
                onDecrement = actions::decRow,
                onAdjustmentClick = actions::changeRowAdjustment,
                onReset = actions::resetRow
            )
        }

        BottomActionButtons(
            onResetAll = actions::resetAll,
            onSave = actions::save
        )
    }
}

@Preview
@Composable
private fun DoubleCounterLandscapePreview() {
    StitchCounterV3Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val fakeActions = object : DoubleCounterActions {
                override fun setTitle(title: String) {}
                override fun setTotalRows(rows: Int) {}
                override fun incStitch() {}
                override fun decStitch() {}
                override fun resetStitch() {}
                override fun changeStitchAdjustment(value: AdjustmentAmount) {}
                override fun incRow() {}
                override fun decRow() {}
                override fun resetRow() {}
                override fun changeRowAdjustment(value: AdjustmentAmount) {}
                override fun resetAll() {}
                override fun save() {}
            }
            
            DoubleCounterLandscapeLayout(
                state = DoubleCounterUiState(
                    title = "Sample Project",
                    stitchCount = 42,
                    stitchAdjustment = AdjustmentAmount.FIVE,
                    rowCount = 10,
                    rowAdjustment = AdjustmentAmount.ONE,
                    totalRows = 20
                ),
                actions = fakeActions
            )
        }
    }
}