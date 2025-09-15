package com.example.stitchcounterv3.feature.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.NavigationEvent
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.domain.usecase.GetProject
import com.example.stitchcounterv3.domain.usecase.UpsertProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class SingleCounterUiState(
    val id: Int = 0,
    val title: String = "",
    val count: Int = 0,
    val adjustment: Int = 1,
    val isLoading: Boolean = false,
)

@HiltViewModel
open class SingleCounterViewModel @Inject constructor(
    private val getProject: GetProject,
    private val upsertProject: UpsertProject,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SingleCounterUiState())
    open val uiState: StateFlow<SingleCounterUiState> = _uiState.asStateFlow()

    // Channel for one-time navigation events
    private val _navigationEvents = Channel<NavigationEvent>(Channel.UNLIMITED)
    val navigationEvents: Flow<NavigationEvent> = _navigationEvents.receiveAsFlow()

    fun load(projectId: Int?) {
        viewModelScope.launch {
            if (projectId == null || projectId == 0) return@launch
            _uiState.value = _uiState.value.copy(isLoading = true)
            val project = getProject(projectId)
            if (project != null) {
                _uiState.value = _uiState.value.copy(
                    id = project.id,
                    title = project.title,
                    count = project.stitchCounterNumber,
                    adjustment = project.stitchAdjustment,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun setTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun changeAdjustment(value: Int) {
        _uiState.value = _uiState.value.copy(adjustment = value)
    }

    fun increment() {
        _uiState.value = _uiState.value.copy(count = _uiState.value.count + _uiState.value.adjustment)
    }

    fun decrement() {
        _uiState.value = _uiState.value.copy(count = (_uiState.value.count - _uiState.value.adjustment).coerceAtLeast(0))
    }

    fun reset() {
        _uiState.value = _uiState.value.copy(count = 0)
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value
            val project = Project(
                id = state.id,
                type = ProjectType.SINGLE,
                title = state.title,
                stitchCounterNumber = state.count,
                stitchAdjustment = state.adjustment,
            )
            val newId = upsertProject(project).toInt()
            if (state.id == 0 && newId > 0) {
                _uiState.value = _uiState.value.copy(id = newId)
            }
        }
    }
    
    // Navigate back to main screen after saving
    fun saveAndGoBack() {
        save()
        viewModelScope.launch {
            _navigationEvents.send(NavigationEvent.PopBackStack)
        }
    }
    
    // Navigate to library screen
    fun goToLibrary() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    com.example.stitchcounterv3.feature.destinations.LibraryScreenDestination()
                )
            )
        }
    }
}

