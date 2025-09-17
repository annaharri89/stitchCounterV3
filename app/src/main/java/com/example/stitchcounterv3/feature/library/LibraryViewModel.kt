package com.example.stitchcounterv3.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.Project
import com.example.stitchcounterv3.domain.model.ProjectType
import com.example.stitchcounterv3.domain.usecase.DeleteProject
import com.example.stitchcounterv3.domain.usecase.ObserveProjects
import com.example.stitchcounterv3.feature.destinations.DoubleCounterScreenDestination
import com.example.stitchcounterv3.feature.destinations.SingleCounterScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryViewModel @Inject constructor(
    observeProjects: ObserveProjects,
    private val deleteProject: DeleteProject,
) : ViewModel() {
    val projects: StateFlow<List<Project>> = observeProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun delete(project: Project) {
        viewModelScope.launch { deleteProject(project) }
    }

    fun navigateToEditProject(project: Project, navigator: DestinationsNavigator) {
        val destination = when (project.type) {
            ProjectType.SINGLE -> SingleCounterScreenDestination(projectId = project.id)
            ProjectType.DOUBLE -> DoubleCounterScreenDestination(projectId = project.id)
        }
        navigator.navigate(destination)
    }
}

