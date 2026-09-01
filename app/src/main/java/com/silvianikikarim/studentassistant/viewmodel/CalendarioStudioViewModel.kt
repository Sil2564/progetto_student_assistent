package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvianikikarim.studentassistant.model.EventoStudio
import com.silvianikikarim.studentassistant.repository.CalendarioStudioRepository
import com.silvianikikarim.studentassistant.repository.FestivitaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarioStudioViewModel(
    private val eventiRepository: CalendarioStudioRepository,
    private val festivitaRepository: FestivitaRepository
) : ViewModel() {

    /** Eventi di studio dell'utente, persistiti su Room: sopravvivono alla chiusura dell'app. */
    val eventi: StateFlow<List<EventoStudio>> = eventiRepository.eventi
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Mappa data -> nome festività, unione di tutti gli anni caricati finora in questa sessione.
    private val _festivita = MutableStateFlow<Map<LocalDate, String>>(emptyMap())
    val festivita: StateFlow<Map<LocalDate, String>> = _festivita.asStateFlow()

    private val anniGiaCaricati = mutableSetOf<Int>()

    fun aggiungiEvento(evento: EventoStudio) {
        viewModelScope.launch {
            eventiRepository.addEvento(evento)
        }
    }

    fun eliminaEvento(evento: EventoStudio) {
        viewModelScope.launch {
            eventiRepository.removeEvento(evento)
        }
    }

    /** Carica le festività dell'anno indicato, solo se non già scaricate in questa sessione. */
    fun caricaFestivitaAnno(anno: Int) {
        if (anno in anniGiaCaricati) return
        anniGiaCaricati += anno

        viewModelScope.launch {
            val nuove = festivitaRepository.getFestivitaAnno(anno)
            _festivita.value = _festivita.value + nuove.associate { it.data to it.nome }
        }
    }
}
