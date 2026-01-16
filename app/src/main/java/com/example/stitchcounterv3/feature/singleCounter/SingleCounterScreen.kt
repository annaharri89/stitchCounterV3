package com.example.stitchcounterv3.feature.singleCounter

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.example.stitchcounterv3.feature.sharedComposables.AdaptiveLayout
import com.ramcosta.composedestinations.annotation.Destination

/**
 * Single Counter Screen - Material 3 Bottom Sheet Implementation
 * 
 * This screen is displayed as a Material 3 bottom sheet that slides up from the bottom
 * of the screen. It maintains all original functionality while providing a modern bottom sheet UX.
 * The bottom sheet is managed by the navigation system in RootNavigationScreen.
 */

@RootNavGraph
@Destination
@Composable
fun SingleCounterScreen(
    projectId: Int? = null,
    viewModel: SingleCounterViewModel = hiltViewModel()
) {
    
    
    LaunchedEffect(projectId) {
        projectId?.let {
            viewModel.loadProject(projectId)
        } ?: run {
            viewModel.resetState()
        }
    }
    
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // The content is wrapped in a Surface with proper height for bottom sheet display
    Surface(
        modifier = Modifier.height(screenHeight * 0.99f)
    ) {
        AdaptiveLayout(
            portraitContent = {
                SingleCounterPortraitLayout(state, viewModel)
            },
            landscapeContent = {
                SingleCounterLandscapeLayout(state, viewModel)
            }
        )
    }
}