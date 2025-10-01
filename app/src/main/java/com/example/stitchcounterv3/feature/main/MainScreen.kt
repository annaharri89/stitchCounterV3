package com.example.stitchcounterv3.feature.main

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stitchcounterv3.feature.navigation.RootNavigationViewModel
import com.example.stitchcounterv3.feature.navigation.SheetScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stitchcounterv3.feature.navigation.RootNavGraph
import com.ramcosta.composedestinations.annotation.Destination

private const val TAG_MAIN_SCREEN = "MainScreen"

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    viewModel: RootNavigationViewModel
) {
    
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Stitch Counter", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = {
                Log.d(TAG_MAIN_SCREEN, "Single Tracker button clicked")
                // Request SingleCounter bottom sheet (no project ID for new projects)
                viewModel.showBottomSheet(SheetScreen.SingleCounter())
            }) {
                Text("New Single Tracker")
            }

            Button(onClick = {
                Log.d(TAG_MAIN_SCREEN, "Double Tracker button clicked")
                // Request DoubleCounter bottom sheet (no project ID for new projects)
                viewModel.showBottomSheet(SheetScreen.DoubleCounter())
            }) {
                Text("New Double Tracker")
            }
        }
    }
}