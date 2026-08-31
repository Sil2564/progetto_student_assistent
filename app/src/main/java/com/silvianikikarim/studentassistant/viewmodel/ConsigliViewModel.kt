package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvianikikarim.studentassistant.model.ArticoloConsiglio
import com.silvianikikarim.studentassistant.model.FraseDelGiorno
import com.silvianikikarim.studentassistant.repository.ConsigliRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Stato della frase del giorno mostrato dalla UI: caricamento, successo o errore. */
sealed interface StatoFrase {
    object Caricamento : StatoFrase
    data class Disponibile(val frase: FraseDelGiorno) : StatoFrase
    object NonDisponibile : StatoFrase
}

class ConsigliViewModel(
    private val repository: ConsigliRepository
) : ViewModel() {

    private val _statoFrase = MutableStateFlow<StatoFrase>(StatoFrase.Caricamento)
    val statoFrase: StateFlow<StatoFrase> = _statoFrase.asStateFlow()

    val articoli: List<ArticoloConsiglio> = repository.articoli

    init {
        caricaFraseDelGiorno()
    }

    fun caricaFraseDelGiorno() {
        _statoFrase.value = StatoFrase.Caricamento
        viewModelScope.launch {
            val frase = repository.getFraseDelGiorno()
            _statoFrase.value = frase?.let { StatoFrase.Disponibile(it) } ?: StatoFrase.NonDisponibile
        }
    }
}
