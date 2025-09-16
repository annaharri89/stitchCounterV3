package com.example.stitchcounterv3.feature.single

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.example.stitchcounterv3.domain.model.NavigationEvent
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stitchcounterv3.feature.navigation.BottomNavGraph
import com.example.stitchcounterv3.feature.sharedComposables.AdaptiveLayout
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@BottomNavGraph
@Destination
@Composable
fun SingleCounterScreen(
    projectId: Int? = null,
    viewModel: SingleCounterViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }
    
    // Collect navigation events and handle them
    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToScreen -> {
                    navigator.navigate(event.destination)
                }
                is NavigationEvent.PopBackStack -> {
                    navigator.popBackStack()
                }
                is NavigationEvent.NavigateUp -> {
                    navigator.popBackStack()
                }
            }
        }
    }
    
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Surface(modifier = Modifier.fillMaxSize()) {
        AdaptiveLayout(
            portraitContent = {
                SingleCounterPortraitScreen(state, viewModel)
            },
            landscapeContent = {
                SingleCounterLandScapeScreen(state, viewModel)
            }
        )
    }
}