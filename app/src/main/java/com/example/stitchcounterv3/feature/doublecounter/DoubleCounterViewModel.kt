package com.example.stitchcounterv3.feature.doublecounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.domain.usecase.GetProject
import com.example.stitchcounterv3.domain.usecase.UpsertProject
import com.example.stitchcounterv3.feature.single.SingleCounterUiState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DoubleCounterUiState(
    val id: Int = 0,
    val title: String = "",
    val stitchCount: Int = 0,
    val stitchAdjustment: AdjustmentAmount = AdjustmentAmount.ONE,
    val rowCount: Int = 0,
    val rowAdjustment: AdjustmentAmount = AdjustmentAmount.ONE,
    val totalRows: Int = 0,
    val isLoading: Boolean = false,
)

@HiltViewModel
open class DoubleCounterViewModel @Inject constructor(
    private val getProject: GetProject,
    private val upsertProject: UpsertProject,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DoubleCounterUiState())
    open val uiState: StateFlow<DoubleCounterUiState> = _uiState.asStateFlow()

    fun loadProject(projectId: Int?) {
        viewModelScope.launch {
            if (projectId == null || projectId == 0) return@launch
            _uiState.update { currentState -> currentState.copy(isLoading = true) }
            val project = getProject(projectId)
            if (project != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        id = project.id,
                        title = project.title,
                        stitchCount = project.stitchCounterNumber,
                        stitchAdjustment = AdjustmentAmount.entries.find { it.adjustmentAmount == project.stitchAdjustment } ?: AdjustmentAmount.ONE,
                        rowCount = project.rowCounterNumber,
                        rowAdjustment = AdjustmentAmount.entries.find { it.adjustmentAmount == project.rowAdjustment } ?: AdjustmentAmount.ONE,
                        totalRows = project.totalRows,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { currentState -> currentState.copy(isLoading = false) }
            }
        }
    }

    fun setTitle(title: String) { _uiState.update { currentState -> currentState.copy(title = title) } }
    fun setTotalRows(rows: Int) { _uiState.update { currentState -> currentState.copy(totalRows = rows.coerceAtLeast(0)) } }

    fun changeStitchAdjustment(value: AdjustmentAmount) { _uiState.update { currentState -> currentState.copy(stitchAdjustment = value) } }//todo lots of repeated code, fix this
    fun changeRowAdjustment(value: AdjustmentAmount) { _uiState.update { currentState -> currentState.copy(rowAdjustment = value) } }//todo lots of repeated code, fix this

    fun incStitch() { _uiState.update { currentState -> currentState.copy(stitchCount = currentState.stitchCount + currentState.stitchAdjustment.adjustmentAmount) } }//todo lots of repeated code, fix this
    fun decStitch() { _uiState.update { currentState -> currentState.copy(stitchCount = (currentState.stitchCount - currentState.stitchAdjustment.adjustmentAmount).coerceAtLeast(0)) } }//todo lots of repeated code, fix this
    fun resetStitch() { _uiState.update { currentState -> currentState.copy(stitchCount = 0) } }//todo lots of repeated code, fix this

    fun incRow() {//todo lots of repeated code, fix this
        val newRow = _uiState.value.rowCount + _uiState.value.rowAdjustment.adjustmentAmount
        _uiState.update { currentState -> currentState.copy(rowCount = newRow) }
    }
    fun decRow() {//todo lots of repeated code, fix this
        val newRow = (_uiState.value.rowCount - _uiState.value.rowAdjustment.adjustmentAmount).coerceAtLeast(0)
        _uiState.update { currentState -> currentState.copy(rowCount = newRow) }
    }
    fun resetRow() { _uiState.update { currentState -> currentState.copy(rowCount = 0) } }//todo lots of repeated code, fix this

    fun save() {
        viewModelScope.launch {
            val s = _uiState.value
            val project = Project(
                id = s.id,
                type = ProjectType.DOUBLE,
                title = s.title,
                stitchCounterNumber = s.stitchCount,
                stitchAdjustment = s.stitchAdjustment.adjustmentAmount,
                rowCounterNumber = s.rowCount,
                rowAdjustment = s.rowAdjustment.adjustmentAmount,
                totalRows = s.totalRows,
            )
            val newId = upsertProject(project).toInt()
            if (s.id == 0 && newId > 0) {
                _uiState.update { currentState -> currentState.copy(id = newId) }
            }
        }
    }

    // Navigate back to main screen after saving
    fun saveAndGoBack(navigator: DestinationsNavigator) {//todo I don't think I need this at all once I have it save on back swipe
        save()
        navigator.popBackStack()
    }

    fun resetState() {
        _uiState.update { _ -> DoubleCounterUiState() }
    }
}

