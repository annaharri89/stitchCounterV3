package com.example.stitchcounterv3.feature.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.annotation.Destination

enum class BottomNavTab(
    val title: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    LIBRARY("Library", Icons.Default.List),
    SETTINGS("Settings", Icons.Default.Settings)
}