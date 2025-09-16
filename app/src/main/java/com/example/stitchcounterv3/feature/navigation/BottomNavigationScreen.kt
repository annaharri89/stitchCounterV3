package com.example.stitchcounterv3.feature.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stitchcounterv3.feature.destinations.LibraryScreenDestination
import com.example.stitchcounterv3.feature.destinations.MainScreenDestination
import com.example.stitchcounterv3.feature.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.example.stitchcounterv3.feature.NavGraphs
import com.ramcosta.composedestinations.annotation.Destination

@RootNavGraph(start = true)
@Destination
@Composable
fun BottomNavigationScreen(
    viewModel: BottomNavigationViewModel = hiltViewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = viewModel::selectTab
            )
        }
    ) { paddingValues ->
        DestinationsNavHost(
            navGraph = NavGraphs.bottom,
            startRoute = when (selectedTab) {
                BottomNavTab.HOME -> MainScreenDestination
                BottomNavTab.LIBRARY -> LibraryScreenDestination
                BottomNavTab.SETTINGS -> SettingsScreenDestination
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
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