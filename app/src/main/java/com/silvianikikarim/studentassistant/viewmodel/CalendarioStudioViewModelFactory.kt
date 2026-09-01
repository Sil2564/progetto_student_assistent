package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.silvianikikarim.studentassistant.repository.CalendarioStudioRepository
import com.silvianikikarim.studentassistant.repository.FestivitaRepository

class CalendarioStudioViewModelFactory(
    private val eventiRepository: CalendarioStudioRepository,
    private val festivitaRepository: FestivitaRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarioStudioViewModel::class.java)) {
            return CalendarioStudioViewModel(eventiRepository, festivitaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
