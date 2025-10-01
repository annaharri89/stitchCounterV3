package com.example.stitchcounterv3.feature.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RootNavigationViewModel : ViewModel() {
    

    private val _currentSheet = MutableStateFlow<SheetScreen?>(null)
    val currentSheet: StateFlow<SheetScreen?> = _currentSheet

    fun showBottomSheet(sheet: SheetScreen?) {
        _currentSheet.value = sheet
    }

    private val _selectedTab = MutableStateFlow(BottomNavTab.HOME)
    val selectedTab: StateFlow<BottomNavTab> = _selectedTab.asStateFlow()

    fun selectTab(tab: BottomNavTab) {
        _selectedTab.value = tab
    }
}