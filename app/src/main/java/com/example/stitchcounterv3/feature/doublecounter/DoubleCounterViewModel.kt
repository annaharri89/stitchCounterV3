package com.example.stitchcounterv3.feature.doublecounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.domain.model.CounterState
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.domain.usecase.GetProject
import com.example.stitchcounterv3.domain.usecase.UpsertProject
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
    val stitchCounterState: CounterState = CounterState(),
    val rowCounterState: CounterState = CounterState(),
    val totalRows: Int = 0,
    val isLoading: Boolean = false,
)

enum class CounterType {
    STITCH,
    ROW
}

@HiltViewModel
open class DoubleCounterViewModel @Inject constructor(
    private val getProject: GetProject,
    private val upsertProject: UpsertProject,
) : ViewModel(), DoubleCounterActions {

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
                        stitchCounterState = CounterState(
                            count = project.stitchCounterNumber,
                            adjustment = AdjustmentAmount.entries.find { it.adjustmentAmount == project.stitchAdjustment } ?: AdjustmentAmount.ONE
                        ),
                        rowCounterState = CounterState(
                            count = project.rowCounterNumber,
                            adjustment = AdjustmentAmount.entries.find { it.adjustmentAmount == project.rowAdjustment } ?: AdjustmentAmount.ONE
                        ),
                        totalRows = project.totalRows,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { currentState -> currentState.copy(isLoading = false) }
            }
        }
    }

    override fun setTitle(title: String) { 
        _uiState.update { currentState -> currentState.copy(title = title) } 
    }
    
    override fun setTotalRows(rows: Int) { 
        _uiState.update { currentState -> currentState.copy(totalRows = rows.coerceAtLeast(0)) } 
    }

    private fun updateCounter(type: CounterType, update: (CounterState) -> CounterState) {
        _uiState.update { currentState ->
            when (type) {
                CounterType.STITCH -> currentState.copy(
                    stitchCounterState = update(currentState.stitchCounterState)
                )
                CounterType.ROW -> currentState.copy(
                    rowCounterState = update(currentState.rowCounterState)
                )
            }
        }
    }

    override fun increment(type: CounterType) = updateCounter(type) { it.increment() }
    override fun decrement(type: CounterType) = updateCounter(type) { it.decrement() }
    override fun reset(type: CounterType) = updateCounter(type) { it.reset() }
    override fun changeAdjustment(type: CounterType, value: AdjustmentAmount) = 
        updateCounter(type) { it.copy(adjustment = value) }

    override fun save() {
        viewModelScope.launch {
            val s = _uiState.value
            val project = Project(
                id = s.id,
                type = ProjectType.DOUBLE,
                title = s.title,
                stitchCounterNumber = s.stitchCounterState.count,
                stitchAdjustment = s.stitchCounterState.adjustment.adjustmentAmount,
                rowCounterNumber = s.rowCounterState.count,
                rowAdjustment = s.rowCounterState.adjustment.adjustmentAmount,
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

    override fun resetAll() {
        reset(CounterType.STITCH)
        reset(CounterType.ROW)
    }
}

