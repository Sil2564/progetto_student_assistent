package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.model.MateriaDao
import kotlinx.coroutines.flow.Flow

/**
 * Unico punto da cui si leggono/creano le materie, condiviso da Appunti e Andamento
 * (e in futuro anche dall'Orario, se diventerà reale). Evita che la stessa materia
 * venga creata due volte per un errore di battitura o maiuscole diverse
 * (es. "Analisi 1" e "analisi 1" restano la stessa materia).
 */
class MateriaRepository(private val materiaDao: MateriaDao) {

    val tutteLeMaterie: Flow<List<Materia>> = materiaDao.getAllMaterie()

    /**
     * Restituisce l'id della materia con questo nome se esiste già (confronto
     * senza distinguere maiuscole/minuscole e ignorando spazi iniziali/finali),
     * altrimenti la crea e restituisce il nuovo id.
     */
    suspend fun getOrCreateMateria(nome: String): Long {
        val nomePulito = nome.trim()
        val esistente = materiaDao.getByNomeIgnoreCase(nomePulito)
        return esistente?.id ?: materiaDao.insertMateria(Materia(nome = nomePulito))
    }

    suspend fun eliminaMateria(materia: Materia) = materiaDao.deleteMateria(materia)

    /**
     * Crea (se non esistono già) tutte le materie passate. Chiamata all'avvio
     * dell'app: idempotente grazie a getOrCreateMateria, quindi richiamarla
     * ad ogni lancio non genera duplicati.
     */
    suspend fun seedMaterieSeNecessario(nomi: List<String>) {
        nomi.forEach { nome -> getOrCreateMateria(nome) }
    }
}
