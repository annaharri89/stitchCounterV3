package com.example.stitchcounterv3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

// Resources - Theme Colors
fun seaCottageLightColors() = lightColorScheme(
    primary = SeaCottageMint,
    secondary = SeaCottageSurf,
    tertiary = SeaCottageWhaleLight,
    onTertiary = SeaCottageWhaleDark,
    //accentDark = SeaCottageWhaleDark,
    //accentLight = SeaCottageWhaleLight,
    background = Color.White.copy(alpha = 0.1f), // Very subtle white overlay
    surface = Color.White.copy(alpha = 0.05f),  // Even more subtle for surfaces
    onPrimary = Color.Black,
    //onSecondary = textSecondaryLight,
)

/*
fun seaCottageDarkColors() = darkColorScheme(
    primary = SeaCottageMint,
    secondary = SeaCottageSurf,
    accentDark = SeaCottageWhaleDark,
    accentLight = SeaCottageWhaleLight,
    background = grey_191919,//todo stitchCounterV2
    surface = grey_262527,//todo stitchCounterV2
    onPrimary = textPrimaryDark,
    onSecondary = textSecondaryDark,
)*/

/* TODO: implement the other theme color
fun retroSummerLightColors() = lightColorScheme(
    primary = RetroSummerOrangeLight,
    secondary = RetroSummerOrangeDark,
    accentDark = RetroSummerCactus,
    accentLight = RetroSummerSun,
    background = Color.White,
    surface = Color.White,
    onPrimary = grey_272525,
    onSecondary = grey_444444,
    cGrey = grey_444444,
)

fun retroSummerDarkColors() = darkColorScheme(
    primary = RetroSummerOrangeLight,
    secondary = retroSummerOrangeDark,
    accentDark = retroSummerCactus,
    accentLight = retroSummerSun,
    background = grey_191919,
    surface = grey_262527,
    onPrimary = Color.White,
    onSecondary = grey_b0b0b0,
    cGrey = grey_444444,
)
*/

@Composable
fun StitchCounterV3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> seaCottageLightColors()
        else -> seaCottageLightColors()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
        /*{

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.secondary,
                                colorScheme.tertiary,
                                colorScheme.primary,
                            )
                        )
                    )
            ) {
                content()
            }
        }*/
    )
}