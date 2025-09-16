package com.example.stitchcounterv3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stitchcounterv3.feature.navigation.BottomNavigationScreen
import com.example.stitchcounterv3.feature.destinations.BottomNavigationScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.example.stitchcounterv3.feature.NavGraphs
import com.example.stitchcounterv3.feature.theme.ThemeViewModel
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val themeUiState by themeViewModel.uiState.collectAsState()
            
            StitchCounterV3Theme(theme = themeUiState.selectedTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
private fun AppContent() {
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        startRoute = BottomNavigationScreenDestination
    )
}