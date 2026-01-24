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
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DoubleCounterUiState(
    val id: Int = 0,
    val title: String = "",
    val stitchCounterState: CounterState = CounterState(),
    val rowCounterState: CounterState = CounterState(),
    val totalRows: Int = 0,
    val isLoading: Boolean = false,
) {
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
    
    private var autoSaveJob: Job? = null
    private val autoSaveDelayMs = 1000L // 1 second debounce

    fun loadProject(projectId: Int?) {
        viewModelScope.launch {
            if (projectId == null || projectId == 0) {
                resetState()
                return@launch
            }
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
        triggerAutoSave()
    }

    override fun increment(type: CounterType) = updateCounter(type) { it.increment() }
    override fun decrement(type: CounterType) = updateCounter(type) { it.decrement() }
    override fun reset(type: CounterType) = updateCounter(type) { it.reset() }
    override fun changeAdjustment(type: CounterType, value: AdjustmentAmount) = 
        updateCounter(type) { it.copy(adjustment = value) }
    
    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        val state = _uiState.value
        if (state.id > 0) {
            autoSaveJob = viewModelScope.launch {
                delay(autoSaveDelayMs)
                save()
            }
        }
    }

    private fun save() {
        viewModelScope.launch {
            val s = _uiState.value
            val existingProject = if (s.id > 0) getProject(s.id) else null
            val project = Project(
                id = s.id,
                type = ProjectType.DOUBLE,
                title = existingProject?.title ?: "",
                stitchCounterNumber = s.stitchCounterState.count,
                stitchAdjustment = s.stitchCounterState.adjustment.adjustmentAmount,
                rowCounterNumber = s.rowCounterState.count,
                rowAdjustment = s.rowCounterState.adjustment.adjustmentAmount,
                totalRows = existingProject?.totalRows ?: s.totalRows,
                imagePath = existingProject?.imagePath
            )
            val newId = upsertProject(project).toInt()
            if (s.id == 0 && newId > 0) {
                _uiState.update { currentState -> currentState.copy(id = newId) }
            }
        }
    }
    
    fun attemptDismissal() {
        viewModelScope.launch {
            autoSaveJob?.cancel()
            val s = _uiState.value
            val existingProject = if (s.id > 0) getProject(s.id) else null
            val project = Project(
                id = s.id,
                type = ProjectType.DOUBLE,
                title = existingProject?.title ?: "",
                stitchCounterNumber = s.stitchCounterState.count,
                stitchAdjustment = s.stitchCounterState.adjustment.adjustmentAmount,
                rowCounterNumber = s.rowCounterState.count,
                rowAdjustment = s.rowCounterState.adjustment.adjustmentAmount,
                totalRows = existingProject?.totalRows ?: s.totalRows,
                imagePath = existingProject?.imagePath
            )
            val newId = upsertProject(project).toInt()
            if (s.id == 0 && newId > 0) {
                _uiState.update { currentState -> currentState.copy(id = newId) }
            }
            _dismissalResult.send(DismissalResult.Allowed)
        }
    }

    fun saveAndGoBack(navigator: DestinationsNavigator) {
        viewModelScope.launch {
            save()
            navigator.popBackStack()
        }
    }

    fun resetState() {
        _uiState.update { _ -> DoubleCounterUiState() }
    }

    override fun resetAll() {
        reset(CounterType.STITCH)
        reset(CounterType.ROW)
    }
}

