package com.example.stitchcounterv3.feature.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.stitchcounterv3.domain.model.DismissalResult
import com.example.stitchcounterv3.feature.NavGraphs
import com.example.stitchcounterv3.feature.doublecounter.DoubleCounterScreen
import com.example.stitchcounterv3.feature.singleCounter.SingleCounterScreen
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
    
    // Track whether dismissal is allowed (prevents automatic dismissal)
    // Use MutableState so confirmValueChange can read current value
    val isDismissalAllowedState = remember { mutableStateOf(false) }
    
    // Track if we're currently validating a dismissal request (shared, but we'll check currentSheetScreen)
    val isValidationPending = remember { mutableStateOf(false) }
    
    // Track current sheet value to detect if we're dismissing (Expanded -> Hidden) vs opening (Hidden -> Expanded)
    val currentSheetValue = remember { mutableStateOf<SheetValue?>(null) }
    
    // Track if discard dialog should be shown (shared across both screen types)
    var showDiscardDialog by remember { mutableStateOf(false) }

    // Material3 Bottom Sheet state - block hiding unless explicitly allowed
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { target ->
            // Only validate when trying to hide from Expanded state (user dismissing), not when opening (Hidden -> Expanded)
            val isDismissing = target == SheetValue.Hidden && currentSheetValue.value == SheetValue.Expanded
            if (isDismissing && !isDismissalAllowedState.value) {
                // Block the dismissal and trigger validation
                if (!isValidationPending.value) {
                    isValidationPending.value = true
                }
                false
            } else {
                val allowed = target != SheetValue.Hidden || isDismissalAllowedState.value
                // Update tracked value
                currentSheetValue.value = target
                allowed
            }
        }
    )
    
    // Track sheet state changes
    LaunchedEffect(sheetState.currentValue) {
        currentSheetValue.value = sheetState.currentValue
    }

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

    // Helper function to handle dismissal results (reduces code duplication)
    fun handleDismissalResult(result: DismissalResult) {
        isValidationPending.value = false
        when (result) {
            is DismissalResult.Allowed -> {
                // Set flag and hide - confirmValueChange will now allow it
                isDismissalAllowedState.value = true
                scope.launch {
                    sheetState.hide()
                }
                viewModel.showBottomSheet(null)
            }
            is DismissalResult.Blocked -> {
                // Dismissal blocked - error already shown in UI, keep sheet open
                isDismissalAllowedState.value = false
                // Ensure sheet stays visible if it was trying to dismiss
                if (sheetState.currentValue != SheetValue.Expanded) {
                    scope.launch {
                        sheetState.expand()
                    }
                }
            }
            is DismissalResult.ShowDiscardDialog -> {
                // Show discard confirmation dialog
                showDiscardDialog = true
                // Ensure sheet stays visible
                if (sheetState.currentValue != SheetValue.Expanded) {
                    scope.launch {
                        sheetState.expand()
                    }
                }
            }
        }
    }
    
    // Helper composable to set up dismissal handling for a sheet
    @Composable
    fun <T : SheetScreen> SheetDismissalHandler(
        screen: T,
        onAttemptDismissal: () -> Unit
    ) {
        // Reset dismissal flag when screen changes
        LaunchedEffect(screen) {
            isDismissalAllowedState.value = false
            isValidationPending.value = false
            showDiscardDialog = false
        }
        
        // Trigger validation when pending and this is the active screen
        LaunchedEffect(isValidationPending.value, currentSheetScreen) {
            if (isValidationPending.value && currentSheetScreen == screen) {
                onAttemptDismissal()
            }
        }
    }
    
    // Common ModalBottomSheet configuration
    val sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val sheetModifier = Modifier.fillMaxHeight(0.98f).fillMaxWidth()
    
    // Common onDismissRequest handler
    val onDismissRequestHandler: () -> Unit = {
        // If already allowed, let it dismiss (this handles programmatic dismissal after validation)
        if (!isDismissalAllowedState.value) {
            // Otherwise, validate first
            isValidationPending.value = true
        }
    }

    // Show bottom sheet if a sheet screen is selected
    currentSheetScreen?.let { screen ->
        when (screen) {
            is SheetScreen.SingleCounter -> {
                val singleCounterViewModel = hiltViewModel<com.example.stitchcounterv3.feature.singleCounter.SingleCounterViewModel>()
                
                SheetDismissalHandler(
                    screen = screen,
                    onAttemptDismissal = { singleCounterViewModel.attemptDismissal() }
                )
                
                // Collect dismissal results
                LaunchedEffect(screen) {
                    singleCounterViewModel.dismissalResult.collect { result ->
                        handleDismissalResult(result)
                    }
                }
                
                ModalBottomSheet(
                    onDismissRequest = onDismissRequestHandler,
                    sheetState = sheetState,
                    shape = sheetShape,
                    modifier = sheetModifier
                ) {
                    SingleCounterScreen(
                        projectId = screen.projectId,
                        viewModel = singleCounterViewModel
                    )
                }
            }
            is SheetScreen.DoubleCounter -> {
                val doubleCounterViewModel = hiltViewModel<com.example.stitchcounterv3.feature.doublecounter.DoubleCounterViewModel>()
                
                SheetDismissalHandler(
                    screen = screen,
                    onAttemptDismissal = { doubleCounterViewModel.attemptDismissal() }
                )
                
                // Collect dismissal results
                LaunchedEffect(screen) {
                    doubleCounterViewModel.dismissalResult.collect { result ->
                        handleDismissalResult(result)
                    }
                }
                
                ModalBottomSheet(
                    onDismissRequest = onDismissRequestHandler,
                    sheetState = sheetState,
                    shape = sheetShape,
                    modifier = sheetModifier
                ) {
                    DoubleCounterScreen(
                        projectId = screen.projectId,
                        viewModel = doubleCounterViewModel
                    )
                }
            }
        }

        // Automatically show sheet when currentSheetScreen changes
        LaunchedEffect(currentSheetScreen) {
            if (currentSheetScreen != null) {
                isDismissalAllowedState.value = false // Reset flag when showing new sheet
                showDiscardDialog = false
                try {
                    sheetState.show()
                } catch (e: Exception) {
                    // Handle error silently
                }
            }
        }
    }
    
    // Discard confirmation dialog (shown outside sheet for proper rendering)
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard project?") },
            text = { Text("This project has no title. Do you want to discard it?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        isDismissalAllowedState.value = true
                        scope.launch {
                            sheetState.hide()
                        }
                        viewModel.showBottomSheet(null)
                    }
                ) {
                    Text("Discard")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDiscardDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Simple sealed class to track which sheet is open
sealed class SheetScreen {
    data class SingleCounter(val projectId: Int? = null) : SheetScreen()
    data class DoubleCounter(val projectId: Int? = null) : SheetScreen()
}