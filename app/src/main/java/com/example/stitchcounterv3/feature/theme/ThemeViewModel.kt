package com.example.stitchcounterv3.feature.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.data.repo.ThemePreferencesRepository
import com.example.stitchcounterv3.domain.model.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * App-wide theme ViewModel that observes theme changes from DataStore.
 * Used in MainActivity to provide the current theme to StitchCounterV3Theme.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferencesRepository: ThemePreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThemeUiState())
    val uiState: StateFlow<ThemeUiState> = _uiState.asStateFlow()

    init {
        observeTheme()
    }

    /**
     * Observes theme changes from DataStore and updates UI state
     */
    private fun observeTheme() {
        viewModelScope.launch {
            themePreferencesRepository.selectedTheme.collect { theme ->
                _uiState.update { currentState -> currentState.copy(selectedTheme = theme) }
            }
        }
    }
}

data class ThemeUiState(
    val selectedTheme: AppTheme = AppTheme.SEA_COTTAGE
)