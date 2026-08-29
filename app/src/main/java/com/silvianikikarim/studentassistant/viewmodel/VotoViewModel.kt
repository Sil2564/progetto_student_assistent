package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.model.Voto
import com.silvianikikarim.studentassistant.model.VotoConMateria
import com.silvianikikarim.studentassistant.repository.VotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VotoViewModel(private val repository: VotoRepository) : ViewModel() {

    /** Stessa lista di materie usata dagli Appunti: nessuna creazione libera qui, sono preset. */
    val tutteLeMaterie: StateFlow<List<Materia>> = repository.tutteLeMaterie
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Tutti i voti con nome materia già risolto: usati per la media generale (Andamento, Impostazioni). */
    val votiConMateria: StateFlow<List<VotoConMateria>> = repository.votiConMateria
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Flow "grezzo" per materia: la screen di dettaglio lo colleziona con collectAsState(). */
    fun votiByMateria(materiaId: Long): Flow<List<Voto>> = repository.votiByMateria(materiaId)

    /** Salva un voto per una materia già esistente e scelta dall'elenco (nessun testo libero). */
    fun inserisciVoto(materiaId: Long, voto: Int, data: String, descrizione: String, note: String) {
        viewModelScope.launch {
            repository.inserisciPerMateria(materiaId, voto, data, descrizione, note)
        }
    }

    fun eliminaVoto(voto: Voto) {
        viewModelScope.launch {
            repository.elimina(voto)
        }
    }

    /** Popola la tabella materie condivisa con quelle dell'anno corrente (idempotente). */
    fun seedMaterieAnnoCorrente() {
        viewModelScope.launch {
            repository.seedMaterieAnnoCorrente()
        }
    }
}
