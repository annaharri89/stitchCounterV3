package com.example.stitchcounterv3.feature.singleCounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.domain.model.CounterState
import com.example.stitchcounterv3.domain.model.DismissalResult
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.domain.usecase.GetProject
import com.example.stitchcounterv3.domain.usecase.UpsertProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SingleCounterUiState(
    val id: Int = 0,
    val title: String = "",
    val titleError: String? = null,
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
        if (!validateTitle()) {
            return
        }
        viewModelScope.launch {
            val state = _uiState.value
            val project = Project(
                id = state.id,
                type = ProjectType.SINGLE,
                title = state.title.trim(),
                stitchCounterNumber = state.counterState.count,
                stitchAdjustment = state.counterState.adjustment.adjustmentAmount,
            )
            val newId = upsertProject(project).toInt()
            if (state.id == 0 && newId > 0) {
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
            val state = _uiState.value
            val project = Project(
                id = state.id,
                type = ProjectType.SINGLE,
                title = state.title.trim(),
                stitchCounterNumber = state.counterState.count,
                stitchAdjustment = state.counterState.adjustment.adjustmentAmount,
            )
            val newId = upsertProject(project).toInt()
            if (state.id == 0 && newId > 0) {
                _uiState.update { currentState -> currentState.copy(id = newId) }
            }
            _dismissalResult.send(DismissalResult.Allowed)
        }
    }

}

