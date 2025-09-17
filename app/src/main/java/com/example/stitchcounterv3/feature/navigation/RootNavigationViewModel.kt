package com.example.stitchcounterv3.feature.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RootNavigationViewModel @Inject constructor() : ViewModel() {
    
    private val _selectedTab = MutableStateFlow(BottomNavTab.HOME)
    val selectedTab: StateFlow<BottomNavTab> = _selectedTab.asStateFlow()
    
    fun selectTab(tab: BottomNavTab) {
        _selectedTab.value = tab
    }
}