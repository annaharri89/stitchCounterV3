package com.example.stitchcounterv3.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.NavigationEvent
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.usecase.DeleteProject
import com.example.stitchcounterv3.domain.usecase.ObserveProjects
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryViewModel @Inject constructor(
    observeProjects: ObserveProjects,
    private val deleteProject: DeleteProject,
) : ViewModel() {
    val projects: StateFlow<List<Project>> = observeProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Channel for one-time navigation events
    private val _navigationEvents = Channel<NavigationEvent>(Channel.UNLIMITED)
    val navigationEvents: Flow<NavigationEvent> = _navigationEvents.receiveAsFlow()

    fun delete(project: Project) {
        viewModelScope.launch { deleteProject(project) }
    }

    fun navigateToEditProject(project: Project) {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    when (project.type) {
                        com.example.stitchcounterv3.domain.model.ProjectType.SINGLE -> 
                            com.example.stitchcounterv3.feature.destinations.SingleCounterScreenDestination(projectId = project.id)
                        com.example.stitchcounterv3.domain.model.ProjectType.DOUBLE -> 
                            com.example.stitchcounterv3.feature.destinations.DoubleCounterScreenDestination(projectId = project.id)
                    }
                )
            )
        }
    }

    fun navigateToNewSingleCounter() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    com.example.stitchcounterv3.feature.destinations.SingleCounterScreenDestination()
                )
            )
        }
    }

    fun navigateToNewDoubleCounter() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    com.example.stitchcounterv3.feature.destinations.DoubleCounterScreenDestination()
                )
            )
        }
    }
}

