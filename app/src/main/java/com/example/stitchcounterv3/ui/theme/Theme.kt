package com.example.stitchcounterv3.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.stitchcounterv3.domain.model.AppTheme
import com.example.stitchcounterv3.feature.theme.ThemeManager

// Resources - Theme Colors
fun seaCottageLightColors() = lightColorScheme(
    primary = SeaCottageSurf40,
    secondary = SeaCottageMint40,
    tertiary = SeaCottageWhaleLight40,
    primaryContainer = SeaCottagePrimaryContainer40,
    error = SeaCottageWhaleDark40,
    onError = Color.White
)


fun seaCottageDarkColors() = darkColorScheme(
    primary = SeaCottageSurf80,
    secondary = SeaCottageMint80,
    tertiary = SeaCottageWhaleLight80,
    primaryContainer = SeaCottagePrimaryContainer80,
    error = SeaCottageWhaleDark80,
    onError = Color.White
)

fun retroSummerLightColors() = lightColorScheme(
    primary =  RetroSummerCactus40 ,
    secondary = RetroSummerSun40,
    tertiary = RetroSummerOrangeLight40,
    onTertiary = RetroSummerSun40,
    primaryContainer = RetroSummerPrimaryContainer40,
    error = RetroSummerOrangeDark40
)

fun retroSummerDarkColors() = darkColorScheme(
    primary = RetroSummerCactus80,
    secondary = RetroSummerSun80,
    tertiary = RetroSummerOrangeLight80,
    primaryContainer = RetroSummerPrimaryContainer80,
    error = RetroSummerOrangeDark80
)

fun purpleLightColors() = lightColorScheme(
    primary =  Purple40 ,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    primaryContainer = PurplePrimaryContainer40,
    error = PurpleViolet40
)

fun purpleDarkColors() = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    primaryContainer = PurplePrimaryContainer80,
    error = PurpleViolet80
)


/**
 * Main theme composable that applies the selected color scheme app-wide.
 * 
 * Flow: ThemeViewModel observes DataStore → emits selected theme → 
 * ThemeManager maps theme to ColorScheme → MaterialTheme applies colors
 */
@Composable
fun StitchCounterV3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    theme: AppTheme = AppTheme.SEA_COTTAGE,
    content: @Composable () -> Unit
) {
    val themeManager = ThemeManager()
    val colorScheme = when {
        darkTheme -> themeManager.getDarkColorScheme(theme)
        else -> themeManager.getLightColorScheme(theme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}