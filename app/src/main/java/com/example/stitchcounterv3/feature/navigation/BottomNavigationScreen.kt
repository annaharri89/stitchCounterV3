package com.example.stitchcounterv3.feature.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.stitchcounterv3.feature.destinations.LibraryScreenDestination
import com.example.stitchcounterv3.feature.destinations.MainScreenDestination
import com.example.stitchcounterv3.feature.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.example.stitchcounterv3.feature.NavGraphs
import com.ramcosta.composedestinations.navigation.navigate

@Composable
fun BottomNavigationScreen(
    viewModel: BottomNavigationViewModel = hiltViewModel()
) {
    val selectedTab = viewModel.selectedTab.collectAsStateWithLifecycle().value
    val configuration = LocalConfiguration.current
    val isCompact = configuration.screenWidthDp < 600 // 600dp is the breakpoint for tablets
    
    // Single navigation controller that persists across orientation changes
    val navController = rememberNavController()
    
    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            BottomNavTab.HOME -> navController.navigate(MainScreenDestination())
            BottomNavTab.LIBRARY -> navController.navigate(LibraryScreenDestination())
            BottomNavTab.SETTINGS -> navController.navigate(SettingsScreenDestination())
        }
    }
    
    if (isCompact) {
        // Use bottom navigation for compact screens (phones in portrait)
        BottomNavigationLayout(
            selectedTab = selectedTab,
            onTabSelected = viewModel::selectTab,
            navController = navController
        )
    } else {
        // Use navigation rail for expanded screens (tablets, phones in landscape)
        NavigationRailLayout(
            selectedTab = selectedTab,
            onTabSelected = viewModel::selectTab,
            navController = navController
        )
    }
}

@Composable
private fun BottomNavigationLayout(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight }
                )
            ) {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }
        }
    ) { paddingValues ->
        DestinationsNavHost(
            navGraph = NavGraphs.bottom,
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
private fun NavigationRailLayout(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Navigation Rail
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }
                )
            ) {
                NavigationRailComponent(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }
            
            // Content area
            DestinationsNavHost(
                navGraph = NavGraphs.bottom,
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    NavigationBar {
        BottomNavTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(tab.title)
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.Gray
                )
            )
        }
    }
}

@Composable
private fun NavigationRailComponent(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    NavigationRail {
        BottomNavTab.entries.forEach { tab ->
            NavigationRailItem(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title
                    )
                },
                label = {
                    Text(tab.title)
                },
                colors = NavigationRailItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.Gray
                )
            )
        }
    }
}