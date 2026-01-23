package com.example.stitchcounterv3.feature.doublecounter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.example.stitchcounterv3.feature.sharedComposables.AdaptiveLayout
import com.ramcosta.composedestinations.annotation.Destination

@RootNavGraph
@Destination
@Composable
fun DoubleCounterScreen(
    projectId: Int? = null,
    viewModel: DoubleCounterViewModel = hiltViewModel(),
    onNavigateToDetail: ((Int) -> Unit)? = null
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

    Surface(
        modifier = Modifier.height(screenHeight * 0.99f)
    ) {
        Box {
            AdaptiveLayout(
                portraitContent = {
                    DoubleCounterPortraitLayout(
                        state = state,
                        actions = viewModel
                    )
                },
                landscapeContent = {
                    DoubleCounterLandscapeLayout(
                        state = state,
                        actions = viewModel
                    )
                }
            )
            if (state.id > 0 && onNavigateToDetail != null) {
                FloatingActionButton(
                    onClick = {
                        onNavigateToDetail(state.id)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Project details"
                    )
                }
            }
        }
    }
}


