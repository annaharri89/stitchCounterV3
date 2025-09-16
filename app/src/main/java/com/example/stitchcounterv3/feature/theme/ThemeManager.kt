package com.example.stitchcounterv3.feature.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.example.stitchcounterv3.domain.model.AppTheme
import com.example.stitchcounterv3.ui.theme.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central manager for theme-related operations.
 * Maps AppTheme enum to actual ColorSchemes and provides color information for UI display.
 */
@Singleton
class ThemeManager @Inject constructor() {

    fun getLightColorScheme(theme: AppTheme): ColorScheme {
        return when (theme) {
            AppTheme.SEA_COTTAGE -> seaCottageLightColors()
            AppTheme.RETRO_SUMMER -> retroSummerLightColors()
            AppTheme.PURPLE -> purpleLightColors()
        }
    }

    fun getDarkColorScheme(theme: AppTheme): ColorScheme {
        return when (theme) {
            AppTheme.SEA_COTTAGE -> seaCottageDarkColors()
            AppTheme.RETRO_SUMMER -> retroSummerDarkColors()
            AppTheme.PURPLE -> purpleDarkColors()
        }
    }
    
    /**
     * Returns color information for UI display (Settings screen color swatches)
     */
    fun getThemeColors(theme: AppTheme): List<ThemeColor> {
        return when (theme) {
            AppTheme.SEA_COTTAGE -> listOf(
                ThemeColor("Mint", SeaCottageMint40, SeaCottageMint80),
                ThemeColor("Surf", SeaCottageSurf40, SeaCottageSurf80),
                ThemeColor("Whale Light", SeaCottageWhaleLight40, SeaCottageWhaleLight80),
                ThemeColor("Whale Dark", SeaCottageWhaleDark40, SeaCottageWhaleDark80)
            )
            AppTheme.RETRO_SUMMER -> listOf(
                ThemeColor("Cactus", RetroSummerCactus40, RetroSummerCactus80),
                ThemeColor("Sun", RetroSummerSun40, RetroSummerSun80),
                ThemeColor("Orange Light", RetroSummerOrangeLight40, RetroSummerOrangeLight80),
                ThemeColor("Orange Dark", RetroSummerOrangeDark40, RetroSummerOrangeDark80)
            )
            AppTheme.PURPLE -> listOf(
                ThemeColor("Purple", Purple40, Purple80),
                ThemeColor("Purple Grey", PurpleGrey40, PurpleGrey80),
                ThemeColor("Pink", Pink40, Pink80),
                ThemeColor("Violet", PurpleViolet40, PurpleViolet80)
            )
        }
    }
}

/**
 * Data class representing a color in a theme with both light and dark variants
 */
data class ThemeColor(
    val name: String,
    val lightColor: androidx.compose.ui.graphics.Color,
    val darkColor: androidx.compose.ui.graphics.Color
)