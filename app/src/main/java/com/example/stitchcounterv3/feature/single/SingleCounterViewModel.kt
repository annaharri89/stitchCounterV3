package com.example.stitchcounterv3.feature.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
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
    val count: Int = 0,
    val adjustment: AdjustmentAmount = AdjustmentAmount.ONE,
    val isLoading: Boolean = false,
)

@HiltViewModel
open class SingleCounterViewModel @Inject constructor(
    private val getProject: GetProject,
    private val upsertProject: UpsertProject,
) : ViewModel() {
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
                        count = project.stitchCounterNumber,
                        adjustment = AdjustmentAmount.entries.find { it.adjustmentAmount == project.stitchAdjustment } ?: AdjustmentAmount.ONE,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { currentState -> currentState.copy(isLoading = false) }
            }
        }
    }

    fun setTitle(title: String) {
        _uiState.update { currentState -> currentState.copy(title = title) }
    }

    fun changeAdjustment(value: AdjustmentAmount) {
        _uiState.update { currentState -> currentState.copy(adjustment = value) }
    }

    fun increment() {
        _uiState.update { currentState -> currentState.copy(count = currentState.count + currentState.adjustment.adjustmentAmount) }
    }

    fun decrement() {
        _uiState.update { currentState -> currentState.copy(count = (currentState.count - currentState.adjustment.adjustmentAmount).coerceAtLeast(0)) }
    }

    fun resetCount() {
        _uiState.update { currentState -> currentState.copy(count = 0) }
    }

    fun resetState() {
        _uiState.update { _ -> SingleCounterUiState() }
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            val project = Project(
                id = state.id,
                type = ProjectType.SINGLE,
                title = state.title,
                stitchCounterNumber = state.count,
                stitchAdjustment = state.adjustment.adjustmentAmount,
            )
            val newId = upsertProject(project).toInt()
            if (state.id == 0 && newId > 0) {
                _uiState.update { currentState -> currentState.copy(id = newId) }
            }
        }
    }

}

