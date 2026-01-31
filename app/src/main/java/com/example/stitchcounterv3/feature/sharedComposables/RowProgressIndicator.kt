package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier
) {
    progress?.let {
        LinearProgressIndicator(
            modifier = modifier.fillMaxWidth(),
            progress = { it }
        )
    }
}
