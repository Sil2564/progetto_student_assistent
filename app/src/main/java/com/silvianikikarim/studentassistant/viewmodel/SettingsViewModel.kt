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

class SettingsViewModelFactory(private val dataStore: SettingsDataStore) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
