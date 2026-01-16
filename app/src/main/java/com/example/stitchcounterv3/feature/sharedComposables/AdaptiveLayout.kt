package com.example.stitchcounterv3.feature.sharedComposables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/** Handles orientation changes in the user's phone.**/
@Composable
fun AdaptiveLayout(
    portraitContent: @Composable ColumnScope.() -> Unit,
    landscapeContent: @Composable RowScope.() -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    if (isLandscape) {
        // Horizontal layout for landscape
        Row(content = landscapeContent)
    } else {
        // Vertical layout for portrait
        Column(content = portraitContent)
    }
}