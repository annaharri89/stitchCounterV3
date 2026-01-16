package com.example.stitchcounterv3.feature.sharedComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stitchcounterv3.ui.theme.quaternary

@Composable
fun IncreaseDecreaseButtons(
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    buttonSpacing: Int = 24,
    buttonShape: RoundedCornerShape = RoundedCornerShape(12.dp),
    incrementFontSize: Int = 50,
    decrementFontSize: Int = 60,
    increaseDecreaseButtonsAspectRatio: Float = 1f
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // + and - buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement button
            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.quaternary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f),
                onClick = onDecrement,
                shape = buttonShape
            ) {
                androidx.compose.material3.Text(
                    text = "-",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = decrementFontSize.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            
            // Increment button
            Button(
                modifier = Modifier
                    .weight(1f),
                    //.aspectRatio(2f),
                onClick = onIncrement,
                shape = buttonShape
            ) {
                androidx.compose.material3.Text(
                    text = "+",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = incrementFontSize.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}