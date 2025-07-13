package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvianikikarim.studentassistant.model.Voto
import com.silvianikikarim.studentassistant.repository.VotoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VotoViewModel(private val repository: VotoRepository) : ViewModel() {

    val tuttiIVoti: StateFlow<List<Voto>> = repository.tuttiIVoti
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun inserisciVoto(voto: Voto) {
        viewModelScope.launch {
            repository.inserisci(voto)
        }
    }

    fun eliminaVoto(voto: Voto) {
        viewModelScope.launch {
            repository.elimina(voto)
        }
    }

}
