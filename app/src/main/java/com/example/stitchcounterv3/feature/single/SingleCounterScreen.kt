package com.example.stitchcounterv3.feature.single

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Single Counter Screen - Material 3 Bottom Sheet Implementation
 * 
 * This screen is displayed as a Material 3 bottom sheet that slides up from the bottom
 * of the screen. It maintains all original functionality while providing a modern bottom sheet UX.
 * The bottom sheet is managed by the navigation system in RootNavigationScreen.
 */
private const val TAG_SINGLE_COUNTER_SCREEN = "SingleCounterScreen"

@RootNavGraph
@Destination
@Composable
fun SingleCounterScreen(
    projectId: Int? = null,
    viewModel: SingleCounterViewModel = hiltViewModel()
) {
    
    Log.d(TAG_SINGLE_COUNTER_SCREEN, "SingleCounterScreen composable called with projectId: $projectId")
    
    LaunchedEffect(projectId) {
        Log.d(TAG_SINGLE_COUNTER_SCREEN, "LaunchedEffect triggered, loading project: $projectId")
        viewModel.loadProject(projectId)
    }
    
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Log.d(TAG_SINGLE_COUNTER_SCREEN, "UI state collected: $state")
    
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Log.d(TAG_SINGLE_COUNTER_SCREEN, "Screen height: $screenHeight")

    // The content is wrapped in a Surface with proper height for bottom sheet display
    Surface(
        modifier = Modifier.height(screenHeight * 0.99f)
    ) {
        Log.d(TAG_SINGLE_COUNTER_SCREEN, "Rendering Surface with AdaptiveLayout")
        AdaptiveLayout(
            portraitContent = {
                Log.d(TAG_SINGLE_COUNTER_SCREEN, "Rendering portrait layout")
                SingleCounterPortraitLayout(state, viewModel)
            },
            landscapeContent = {
                Log.d(TAG_SINGLE_COUNTER_SCREEN, "Rendering landscape layout")
                SingleCounterLandscapeLayout(state, viewModel)
            }
        )
    }
}