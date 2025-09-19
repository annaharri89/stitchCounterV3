package com.example.stitchcounterv3.feature.single

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min
import com.example.stitchcounterv3.domain.model.AdjustmentAmount
import com.example.stitchcounterv3.feature.sharedComposables.AdjustmentButtons
import com.example.stitchcounterv3.ui.theme.StitchCounterV3Theme

@Composable
fun SingleCounterLandscapeLayout(
    state: SingleCounterUiState,
    viewModel: SingleCounterViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Left side - Project info and controls
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.setTitle(it) },
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth()
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Take up available space
                contentAlignment = Alignment.Center
            ) {
                val availableHeight = maxHeight
                val availableWidth = maxWidth
                
                // Calculate font size based on available space
                // Use most of the available space for the text
                val fontSize = min(
                    availableHeight.value * 0.8f, // Use 80% of available height
                    availableWidth.value * 0.5f // Use 50% of available width
                ).coerceIn(48f, 300f).sp // Clamp between 48sp and 300sp for larger text
                
                Text(
                    text = "${state.count}",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.save() },
                    modifier = Modifier.weight(1f)
                ) { 
                    Text("Save") 
                }
                
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    ),
                    onClick = { viewModel.reset() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
            }
        }
        
        // Right side - Main counter buttons
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = .85f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { viewModel.decrement() },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 100.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { viewModel.increment() },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 80.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            AdjustmentButtons(
                selectedAdjustmentAmount = state.adjustment,
                onAdjustmentClick = { viewModel.changeAdjustment(it) }
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun SingleCounterLandscapeScreenPreview() {
    StitchCounterV3Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            // Preview the layout directly without ViewModel dependency
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Left side - Project info and controls
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = "Sample Project",
                        onValueChange = { },
                        label = { Text("Project Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Take up available space
                        contentAlignment = Alignment.Center
                    ) {
                        val availableHeight = maxHeight
                        val availableWidth = maxWidth
                        
                        // Calculate font size based on available space
                        val fontSize = min(
                            availableHeight.value * 0.8f,
                            availableWidth.value * 0.5f
                        ).coerceIn(48f, 300f).sp
                        
                        Text(
                            text = "42",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = fontSize,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) { 
                            Text("Save") 
                        }
                        
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = androidx.compose.ui.graphics.Color.White
                            ),
                            onClick = { },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset")
                        }
                    }
                }
                
                // Right side - Main counter buttons
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = .85f),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "-",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = 100.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            onClick = { },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = 80.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    AdjustmentButtons(
                        selectedAdjustmentAmount = AdjustmentAmount.FIVE,
                        onAdjustmentClick = { }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}