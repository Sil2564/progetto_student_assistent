package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.EventoStudio
import com.silvianikikarim.studentassistant.model.EventoStudioDao
import kotlinx.coroutines.flow.Flow

class CalendarioStudioRepository(
    private val dao: EventoStudioDao
) {
    val eventi: Flow<List<EventoStudio>> = dao.getAllEventi()

    suspend fun addEvento(evento: EventoStudio) {
        dao.insertEvento(evento)
    }

    suspend fun removeEvento(evento: EventoStudio) {
        dao.deleteEvento(evento)
    }
}
