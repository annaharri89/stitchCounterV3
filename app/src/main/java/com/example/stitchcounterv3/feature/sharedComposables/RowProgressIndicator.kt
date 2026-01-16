package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A horizontal progress indicator that shows row progress.
 * Only displays when progress is not null.
 * 
 * @param progress The progress value (0f to 1f), or null to hide the indicator
 * @param modifier Modifier to be applied to the progress indicator
 */
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
