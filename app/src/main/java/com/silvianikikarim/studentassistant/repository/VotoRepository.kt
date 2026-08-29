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

    suspend fun inserisciPerMateria(materiaId: Long, voto: Int, data: String, descrizione: String, note: String) {
        votoDao.insertVoto(
            Voto(
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

    suspend fun seedMaterieAnnoCorrente() {
        materiaRepository.seedMaterieSeNecessario(
            com.silvianikikarim.studentassistant.model.MaterieAnnoCorrente.nomi
        )
    }
}
