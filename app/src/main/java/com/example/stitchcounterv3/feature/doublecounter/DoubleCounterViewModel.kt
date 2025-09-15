package com.example.stitchcounterv3.feature.doublecounter

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

data class DoubleCounterUiState(
    val id: Int = 0,
    val title: String = "",
    val stitchCount: Int = 0,
    val stitchAdjustment: Int = 1,
    val rowCount: Int = 0,
    val rowAdjustment: Int = 1,
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
                    stitchCount = project.stitchCounterNumber,
                    stitchAdjustment = project.stitchAdjustment,
                    rowCount = project.rowCounterNumber,
                    rowAdjustment = project.rowAdjustment,
                    totalRows = project.totalRows,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun setTitle(title: String) { _uiState.value = _uiState.value.copy(title = title) }
    fun setTotalRows(rows: Int) { _uiState.value = _uiState.value.copy(totalRows = rows.coerceAtLeast(0)) }

    fun changeStitchAdjustment(value: Int) { _uiState.value = _uiState.value.copy(stitchAdjustment = value) }
    fun changeRowAdjustment(value: Int) { _uiState.value = _uiState.value.copy(rowAdjustment = value) }

    fun incStitch() { _uiState.value = _uiState.value.copy(stitchCount = _uiState.value.stitchCount + _uiState.value.stitchAdjustment) }
    fun decStitch() { _uiState.value = _uiState.value.copy(stitchCount = (_uiState.value.stitchCount - _uiState.value.stitchAdjustment).coerceAtLeast(0)) }
    fun resetStitch() { _uiState.value = _uiState.value.copy(stitchCount = 0) }

    fun incRow() {
        val newRow = _uiState.value.rowCount + _uiState.value.rowAdjustment
        _uiState.value = _uiState.value.copy(rowCount = newRow)
    }
    fun decRow() {
        val newRow = (_uiState.value.rowCount - _uiState.value.rowAdjustment).coerceAtLeast(0)
        _uiState.value = _uiState.value.copy(rowCount = newRow)
    }
    fun resetRow() { _uiState.value = _uiState.value.copy(rowCount = 0) }

    fun save() {
        viewModelScope.launch {
            val s = _uiState.value
            val project = Project(
                id = s.id,
                type = ProjectType.DOUBLE,
                title = s.title,
                stitchCounterNumber = s.stitchCount,
                stitchAdjustment = s.stitchAdjustment,
                rowCounterNumber = s.rowCount,
                rowAdjustment = s.rowAdjustment,
                totalRows = s.totalRows,
            )
            val newId = upsertProject(project).toInt()
            if (s.id == 0 && newId > 0) {
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

