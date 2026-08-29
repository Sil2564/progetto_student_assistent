package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.model.Nota
import com.silvianikikarim.studentassistant.model.NotaDao
import kotlinx.coroutines.flow.Flow

class AppuntiRepository(
    private val materiaRepository: MateriaRepository,
    private val notaDao: NotaDao
) {
    val tutteLeMaterie: Flow<List<Materia>> = materiaRepository.tutteLeMaterie

    /** Se una materia con questo nome esiste già (anche scritta diversamente), la riusa invece di duplicarla. */
    suspend fun inserisciMateria(nome: String): Long = materiaRepository.getOrCreateMateria(nome)

    suspend fun eliminaMateria(materia: Materia) = materiaRepository.eliminaMateria(materia)

    fun noteByMateria(materiaId: Long): Flow<List<Nota>> = notaDao.getNoteByMateria(materiaId)

    suspend fun getNotaById(notaId: Long): Nota? = notaDao.getNotaById(notaId)

    suspend fun inserisciNota(nota: Nota): Long = notaDao.insertNota(nota)

    suspend fun aggiornaNota(nota: Nota) = notaDao.updateNota(nota)

    suspend fun eliminaNota(nota: Nota) = notaDao.deleteNota(nota)
}
