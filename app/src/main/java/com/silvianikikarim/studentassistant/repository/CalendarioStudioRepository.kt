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

    /**
     * Inserisce sessioni di studio ed esami di esempio se la tabella è vuota.
     */
    suspend fun seedEventiSeNecessario() {
        if (dao.countEventi() == 0) {
            val now = java.time.LocalDate.now()
            val zone = java.time.ZoneId.systemDefault()

            val oggiMillis = now.atStartOfDay(zone).toInstant().toEpochMilli()
            val traDueGiorniMillis = now.plusDays(2).atStartOfDay(zone).toInstant().toEpochMilli()
            val prossimaSettimanaMillis = now.plusDays(6).atStartOfDay(zone).toInstant().toEpochMilli()

            dao.insertEvento(
                EventoStudio(
                    titolo = "Studio Sistemi Mobili",
                    materia = "Laboratorio di Programmazione di Sistemi Mobili",
                    data = oggiMillis,
                    oraInizio = "14:00",
                    oraFine = "18:00",
                    tipo = "Studio"
                )
            )
            dao.insertEvento(
                EventoStudio(
                    titolo = "Ripasso Ingegneria del Software",
                    materia = "Progettazione e Sviluppo del Software",
                    data = traDueGiorniMillis,
                    oraInizio = "10:00",
                    oraFine = "13:00",
                    tipo = "Ripasso"
                )
            )
            dao.insertEvento(
                EventoStudio(
                    titolo = "Preparazione Progetto Web",
                    materia = "Ingegneria dei Sistemi Web",
                    data = prossimaSettimanaMillis,
                    oraInizio = "15:00",
                    oraFine = "19:00",
                    tipo = "Studio"
                )
            )
        }
    }
}
