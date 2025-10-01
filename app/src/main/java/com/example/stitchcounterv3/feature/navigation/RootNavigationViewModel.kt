package com.example.stitchcounterv3.feature.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RootNavigationViewModel : ViewModel() {
    
    companion object {
        private const val TAG = "RootNavigationVM"
    }

    private val _currentSheet = MutableStateFlow<SheetScreen?>(null)
    val currentSheet: StateFlow<SheetScreen?> = _currentSheet

    fun showBottomSheet(sheet: SheetScreen?) {
        Log.d(TAG, "showBottomSheet called with: $sheet")
        _currentSheet.value = sheet
        Log.d(TAG, "currentSheet value updated to: ${_currentSheet.value}")
    }

    private val _selectedTab = MutableStateFlow(BottomNavTab.HOME)
    val selectedTab: StateFlow<BottomNavTab> = _selectedTab.asStateFlow()

    fun selectTab(tab: BottomNavTab) {
        Log.d(TAG, "selectTab called with: $tab")
        _selectedTab.value = tab
    }
}