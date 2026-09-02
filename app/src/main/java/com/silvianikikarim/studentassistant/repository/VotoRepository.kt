package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.Materia
import com.silvianikikarim.studentassistant.model.Voto
import com.silvianikikarim.studentassistant.model.VotoConMateria
import com.silvianikikarim.studentassistant.model.VotoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class VotoRepository(
    private val votoDao: VotoDao,
    private val materiaRepository: MateriaRepository
) {

    /** Stessa identica lista di materie vista da Appunti: nessun duplicato, unica fonte di verità. */
    val tutteLeMaterie: Flow<List<Materia>> = materiaRepository.tutteLeMaterie

    /** Voti "arricchiti" col nome della materia: usati per la media generale e le statistiche. */
    val votiConMateria: Flow<List<VotoConMateria>> = combine(
        votoDao.getAllVoti(),
        materiaRepository.tutteLeMaterie
    ) { voti, materie ->
        val mappaMaterie = materie.associateBy { it.id }
        voti.map { voto ->
            VotoConMateria(
                voto = voto,
                nomeMateria = mappaMaterie[voto.materiaId]?.nome ?: "Materia eliminata"
            )
        }
    }

    /** Voti di UNA sola materia (usato nella schermata di dettaglio materia). */
    fun votiByMateria(materiaId: Long): Flow<List<Voto>> = votoDao.getVotiByMateria(materiaId)

    /**
     * Salva il voto di una materia. Se idEsistente è diverso da 0, sovrascrive
     * quel voto (modifica); altrimenti ne crea uno nuovo. Serve per il vincolo
     * "un solo voto per materia": la UI passa sempre l'id del voto già presente
     * per quella materia, se c'è.
     */
    suspend fun inserisciPerMateria(
        materiaId: Long,
        voto: Int,
        data: String,
        descrizione: String,
        note: String,
        idEsistente: Int = 0
    ) {
        votoDao.insertVoto(
            Voto(
                id = idEsistente,
                materiaId = materiaId,
                voto = voto,
                data = data,
                descrizione = descrizione,
                note = note
            )
        )
    }

    suspend fun elimina(voto: Voto) {
        votoDao.deleteVoto(voto)
    }

    suspend fun seedMaterieCorso() {
        materiaRepository.seedMaterieSeNecessario(
            com.silvianikikarim.studentassistant.model.MaterieCorso.tutte
        )
    }

    /**
     * Inserisce alcuni voti di esempio se il database dei voti è completamente vuoto.
     */
    suspend fun seedVotiSeNecessario() {
        if (votoDao.countVoti() == 0) {
            val votiIniziali = listOf(
                Triple("Programmazione", 30, "2026-02-12"),
                Triple("Basi di Dati", 28, "2026-06-18"),
                Triple("Reti di Calcolatori e Programmazione di Rete", 27, "2026-07-04"),
                Triple("Elementi di Architetture degli Elaboratori e Sistemi Operativi", 29, "2026-07-20")
            )
            votiIniziali.forEach { (materiaNome, votoVal, dataEsame) ->
                val materiaId = materiaRepository.getOrCreateMateria(materiaNome, 1)
                inserisciPerMateria(
                    materiaId = materiaId,
                    voto = votoVal,
                    data = dataEsame,
                    descrizione = "Esame superato",
                    note = "Superato al primo appello"
                )
            }
        }
    }
}
