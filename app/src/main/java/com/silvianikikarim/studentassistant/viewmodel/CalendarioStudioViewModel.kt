package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvianikikarim.studentassistant.model.EventoStudio
import com.silvianikikarim.studentassistant.repository.CalendarioStudioRepository
import kotlinx.coroutines.launch

class CalendarioStudioViewModel(
    private val repository: CalendarioStudioRepository
) : ViewModel() {

    val eventi = repository.eventi

    fun aggiungiEvento(evento: EventoStudio) {
        viewModelScope.launch {
            repository.addEvento(evento)
        }
    }

    fun eliminaEvento(evento: EventoStudio) {
        viewModelScope.launch {
            repository.removeEvento(evento)
        }
    }
}
