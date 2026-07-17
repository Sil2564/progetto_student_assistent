package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.silvianikikarim.studentassistant.repository.AppuntiRepository

class AppuntiViewModelFactory(
    private val repository: AppuntiRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppuntiViewModel::class.java)) {
            return AppuntiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
