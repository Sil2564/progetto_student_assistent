package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.model.MateriaDao
import com.silvianikikarim.studentassistant.model.Nota
import com.silvianikikarim.studentassistant.model.NotaDao
import kotlinx.coroutines.flow.Flow

class AppuntiRepository(
    private val materiaDao: MateriaDao,
    private val notaDao: NotaDao
) {
    val tutteLeMaterie: Flow<List<Materia>> = materiaDao.getAllMaterie()

    suspend fun inserisciMateria(materia: Materia): Long = materiaDao.insertMateria(materia)

    suspend fun eliminaMateria(materia: Materia) = materiaDao.deleteMateria(materia)

    fun noteByMateria(materiaId: Long): Flow<List<Nota>> = notaDao.getNoteByMateria(materiaId)

    suspend fun getNotaById(notaId: Long): Nota? = notaDao.getNotaById(notaId)

    suspend fun inserisciNota(nota: Nota): Long = notaDao.insertNota(nota)

    suspend fun aggiornaNota(nota: Nota) = notaDao.updateNota(nota)

    suspend fun eliminaNota(nota: Nota) = notaDao.deleteNota(nota)
}
