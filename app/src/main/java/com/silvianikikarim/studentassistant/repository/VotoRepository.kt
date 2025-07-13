package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.Voto
import com.silvianikikarim.studentassistant.model.VotoDao
import kotlinx.coroutines.flow.Flow

class VotoRepository(private val votoDao: VotoDao) {

    val tuttiIVoti: Flow<List<Voto>> = votoDao.getAllVoti()

    suspend fun inserisci(voto: Voto) {
        votoDao.insertVoto(voto)
    }

    suspend fun elimina(voto: Voto) {
        votoDao.deleteVoto(voto)
    }

}
