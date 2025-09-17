package com.example.stitchcounterv3.feature.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stitchcounterv3.feature.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost


@Composable
fun RootNavigationScreen(viewModel: RootNavigationViewModel = hiltViewModel()) {
    val selectedTab = viewModel.selectedTab.collectAsStateWithLifecycle().value
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600 // 600dp is the breakpoint for tablets

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // Use bottom navigation for compact screens (phones in portrait)
            if (isCompact) {
                BottomNavigationLayout(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::selectTab,
                    navController = navController
                )
            }
        }
    ) { innerPadding ->

        Row(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (!isCompact) {
                // Use navigation rail for expanded screens (tablets, phones in landscape)
                NavigationRailLayout(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::selectTab,
                    navController = navController
                )
            }

            DestinationsNavHost(
                navGraph = NavGraphs.root,
                navController = navController
            )
        }
    }
}

