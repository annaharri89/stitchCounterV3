package com.example.stitchcounterv3.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.data.repo.ThemePreferencesRepository
import com.example.stitchcounterv3.domain.model.AppTheme
import com.example.stitchcounterv3.feature.theme.ThemeColor
import com.example.stitchcounterv3.feature.theme.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Settings screen ViewModel that manages theme selection and displays color information.
 * 
 * Flow: User taps theme → onThemeSelected() → DataStore saves → 
 * observeTheme() updates UI → SettingsScreen shows new selection
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreferencesRepository: ThemePreferencesRepository,
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        observeTheme()
    }

    /**
     * Observes theme changes and updates UI with current theme + color information
     */
    private fun observeTheme() {
        viewModelScope.launch {
            themePreferencesRepository.selectedTheme.collect { theme ->
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedTheme = theme,
                        themeColors = themeManager.getThemeColors(theme)
                    )
                }
            }
        }
    }

    /**
     * Handles theme selection from UI - saves to DataStore
     */
    fun onThemeSelected(theme: AppTheme) {
        viewModelScope.launch {
            themePreferencesRepository.setTheme(theme)
        }
    }
}

data class SettingsUiState(
    val selectedTheme: AppTheme = AppTheme.SEA_COTTAGE,
    val themeColors: List<ThemeColor> = emptyList()
)