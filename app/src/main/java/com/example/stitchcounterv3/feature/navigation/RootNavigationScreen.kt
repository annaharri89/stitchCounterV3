package com.example.stitchcounterv3.feature.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.stitchcounterv3.feature.NavGraphs
import com.example.stitchcounterv3.feature.doublecounter.DoubleCounterScreen
import com.example.stitchcounterv3.feature.single.SingleCounterScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun RootNavigationScreen(viewModel: RootNavigationViewModel) {

    val selectedTab = viewModel.selectedTab.collectAsStateWithLifecycle().value
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // Track current bottom sheet
    val currentSheetScreen by viewModel.currentSheet.collectAsStateWithLifecycle()

    // Material3 Bottom Sheet state
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Top-level Scaffold
    Scaffold(
        bottomBar = {
            if (isCompact) {
                BottomNavigationLayout(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::selectTab,
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            if (!isCompact) {
                NavigationRailLayout(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::selectTab,
                    navController = navController
                )
            }

            // Main NavHost for normal navigation
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.root,
                engine = rememberAnimatedNavHostEngine(),
                dependenciesContainerBuilder = {
                    dependency(NavGraphs.root) { viewModel }
                }
            )
        }
    }

    // Show bottom sheet if a sheet screen is selected
    currentSheetScreen?.let { screen ->
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    viewModel.showBottomSheet(null)
                }
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            when (screen) {
                is SheetScreen.SingleCounter -> {
                    SingleCounterScreen(
                        projectId = screen.projectId,
                        viewModel = hiltViewModel()
                    )
                }
                is SheetScreen.DoubleCounter -> {
                    DoubleCounterScreen(
                        projectId = screen.projectId,
                        viewModel = hiltViewModel()
                    )
                }
            }
        }

        // Automatically show sheet when currentSheetScreen changes
        LaunchedEffect(currentSheetScreen) {
            try {
                sheetState.show()
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
}

// Simple sealed class to track which sheet is open
sealed class SheetScreen {
    data class SingleCounter(val projectId: Int?) : SheetScreen()
    data class DoubleCounter(val projectId: Int?) : SheetScreen()
}