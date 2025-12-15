package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvianikikarim.studentassistant.util.SettingsDataStore
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: SettingsDataStore
) : ViewModel() {

    val darkMode = dataStore.darkModeFlow

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkMode(enabled)
        }
    }
}
