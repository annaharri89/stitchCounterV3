package com.example.stitchcounterv3.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stitchcounterv3.domain.model.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainViewModel handles business logic for the main screen.
 * 
 * Event-Driven Navigation Paradigm:
 * - ViewModel focuses on business logic only
 * - Navigation events are emitted via Channel for one-time consumption
 * - UI layer collects events and handles navigation
 * - Clean separation of concerns
 * - Testable without navigation dependencies
 * - Single Responsibility Principle
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // Channel for one-time navigation events
    private val _navigationEvents = Channel<NavigationEvent>(Channel.UNLIMITED)
    val navigationEvents: Flow<NavigationEvent> = _navigationEvents.receiveAsFlow()

    fun navigateToSingleCounter() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    com.example.stitchcounterv3.feature.destinations.SingleCounterScreenDestination()
                )
            )
        }
    }

    fun navigateToDoubleCounter() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    com.example.stitchcounterv3.feature.destinations.DoubleCounterScreenDestination()
                )
            )
        }
    }

    fun navigateToLibrary() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    com.example.stitchcounterv3.feature.destinations.LibraryScreenDestination()
                )
            )
        }
    }

    fun navigateToSettings() {
        viewModelScope.launch {
            _navigationEvents.send(
                NavigationEvent.NavigateToScreen(
                    com.example.stitchcounterv3.feature.destinations.SettingsScreenDestination()
                )
            )
        }
    }
}