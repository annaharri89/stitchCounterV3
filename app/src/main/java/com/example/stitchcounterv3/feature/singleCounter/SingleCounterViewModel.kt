package com.example.stitchcounterv3.feature.singleCounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.domain.model.CounterState
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.domain.usecase.GetProject
import com.example.stitchcounterv3.domain.usecase.UpsertProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SingleCounterUiState(
    val id: Int = 0,
    val title: String = "",
    val counterState: CounterState = CounterState(),
    val isLoading: Boolean = false,
)

@HiltViewModel
open class SingleCounterViewModel @Inject constructor(
    private val getProject: GetProject,
    private val upsertProject: UpsertProject,
) : ViewModel(), SingleCounterActions {
    private val _uiState = MutableStateFlow(SingleCounterUiState())
    open val uiState: StateFlow<SingleCounterUiState> = _uiState.asStateFlow()

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
                        counterState = CounterState(
                            count = project.stitchCounterNumber,
                            adjustment = AdjustmentAmount.entries.find { it.adjustmentAmount == project.stitchAdjustment } ?: AdjustmentAmount.ONE
                        ),
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

    override fun changeAdjustment(value: AdjustmentAmount) {
        _uiState.update { currentState ->
            currentState.copy(
                counterState = currentState.counterState.copy(adjustment = value)
            )
        }
    }

    override fun increment() {
        _uiState.update { currentState ->
            currentState.copy(
                counterState = currentState.counterState.increment()
            )
        }
    }

    override fun decrement() {
        _uiState.update { currentState ->
            currentState.copy(
                counterState = currentState.counterState.decrement()
            )
        }
    }

    override fun resetCount() {
        _uiState.update { currentState ->
            currentState.copy(
                counterState = currentState.counterState.reset()
            )
        }
    }

    fun resetState() {
        _uiState.update { _ -> SingleCounterUiState() }
    }

    override fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            val project = Project(
                id = state.id,
                type = ProjectType.SINGLE,
                title = state.title,
                stitchCounterNumber = state.counterState.count,
                stitchAdjustment = state.counterState.adjustment.adjustmentAmount,
            )
            val newId = upsertProject(project).toInt()
            if (state.id == 0 && newId > 0) {
                _uiState.update { currentState -> currentState.copy(id = newId) }
            }
        }
    }

}

