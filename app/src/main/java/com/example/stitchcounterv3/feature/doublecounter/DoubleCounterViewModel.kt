package com.example.stitchcounterv3.feature.doublecounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.domain.model.CounterState
import com.example.stitchcounterv3.domain.model.DismissalResult
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.domain.usecase.GetProject
import com.example.stitchcounterv3.domain.usecase.UpsertProject
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DoubleCounterUiState(
    val id: Int = 0,
    val title: String = "",
    val titleError: String? = null,
    val stitchCounterState: CounterState = CounterState(),
    val rowCounterState: CounterState = CounterState(),
    val totalRows: Int = 0,
    val isLoading: Boolean = false,
) {
    /**
     * Calculated progress for row completion (0f to 1f).
     * Returns null if totalRows is not set (0), indicating progress should not be shown.
     */
    val rowProgress: Float? = if (totalRows > 0) {
        (rowCounterState.count.toFloat() / totalRows.toFloat()).coerceIn(0f, 1f)
    } else {
        null
    }
}

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
    
    private val _dismissalResult = Channel<DismissalResult>(Channel.BUFFERED)
    val dismissalResult = _dismissalResult.receiveAsFlow()

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
        _uiState.update { currentState -> 
            currentState.copy(
                title = title,
                titleError = null // Clear error when user types
            )
        } 
    }
    
    private fun validateTitle(): Boolean {
        val title = _uiState.value.title.trim()
        return if (title.isEmpty()) {
            _uiState.update { it.copy(titleError = "You have to have a title") }
            false
        } else {
            _uiState.update { it.copy(titleError = null) }
            true
        }
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
        if (!validateTitle()) {
            return
        }
        viewModelScope.launch {
            val s = _uiState.value
            val project = Project(
                id = s.id,
                type = ProjectType.DOUBLE,
                title = s.title.trim(),
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
    
    /**
     * Attempts to dismiss the bottom sheet. Validates title and saves if valid.
     * Returns true if dismissal is allowed, false if blocked due to validation error.
     */
    fun attemptDismissal() {
        viewModelScope.launch {
            if (!validateTitle()) {
                // Show discard dialog instead of blocking with error
                _dismissalResult.send(DismissalResult.ShowDiscardDialog)
                return@launch
            }
            
            // Auto-save when dismissing with valid title
            val s = _uiState.value
            val project = Project(
                id = s.id,
                type = ProjectType.DOUBLE,
                title = s.title.trim(),
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
            _dismissalResult.send(DismissalResult.Allowed)
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

