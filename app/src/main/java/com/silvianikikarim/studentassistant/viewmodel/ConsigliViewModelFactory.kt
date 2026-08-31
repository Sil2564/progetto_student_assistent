package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.silvianikikarim.studentassistant.repository.ConsigliRepository

class ConsigliViewModelFactory(
    private val repository: ConsigliRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsigliViewModel::class.java)) {
            return ConsigliViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
