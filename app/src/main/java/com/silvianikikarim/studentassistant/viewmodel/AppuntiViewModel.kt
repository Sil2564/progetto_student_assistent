package com.silvianikikarim.studentassistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.model.Nota
import com.silvianikikarim.studentassistant.repository.AppuntiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppuntiViewModel(private val repository: AppuntiRepository) : ViewModel() {

    val tutteLeMaterie: StateFlow<List<Materia>> = repository.tutteLeMaterie
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun inserisciMateria(nome: String) {
        viewModelScope.launch {
            repository.inserisciMateria(Materia(nome = nome))
        }
    }

    fun eliminaMateria(materia: Materia) {
        viewModelScope.launch {
            repository.eliminaMateria(materia)
        }
    }

    // Flow "grezzo" per materia: la screen lo colleziona con collectAsState(),
    // niente bisogno di uno StateFlow dedicato per ogni materiaId possibile.
    fun noteByMateria(materiaId: Long): Flow<List<Nota>> = repository.noteByMateria(materiaId)

    suspend fun getNotaById(notaId: Long): Nota? = repository.getNotaById(notaId)

    fun inserisciNota(nota: Nota) {
        viewModelScope.launch {
            repository.inserisciNota(nota)
        }
    }

    fun aggiornaNota(nota: Nota) {
        viewModelScope.launch {
            repository.aggiornaNota(nota.copy(dataModifica = System.currentTimeMillis()))
        }
    }

    fun eliminaNota(nota: Nota) {
        viewModelScope.launch {
            repository.eliminaNota(nota)
        }
    }
}
