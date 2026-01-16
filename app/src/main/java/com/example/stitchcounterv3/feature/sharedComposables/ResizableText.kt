package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun ResizableText(
    text: String,
    modifier: Modifier = Modifier,
    heightRatio: Float = 0.8f,
    widthRatio: Float = 0.4f,
    minFontSize: Float = 48f,
    maxFontSize: Float = 300f,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Center
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val availableHeight = maxHeight
        val availableWidth = maxWidth
        
        // Calculate font size based on available space
        val fontSize = min(
            availableHeight.value * heightRatio, // Use specified ratio of available height
            availableWidth.value * widthRatio // Use specified ratio of available width
        ).coerceIn(minFontSize, maxFontSize).sp // Clamp between min and max font sizes
        
        Text(
            text = text,
            maxLines = 1,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = fontSize,
                fontWeight = fontWeight
            ),
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
    }
}