package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.Festivita
import com.silvianikikarim.studentassistant.network.HolidayApi
import java.time.LocalDate

/**
 * Scarica le festività pubbliche italiane per un dato anno da Nager.Date.
 * Mantiene una cache in memoria per anno, così scorrendo i mesi dello stesso
 * anno solare non viene rifatta la stessa chiamata di rete più volte.
 */
class FestivitaRepository(
    private val api: HolidayApi
) {
    private val cachePerAnno = mutableMapOf<Int, List<Festivita>>()

    suspend fun getFestivitaAnno(anno: Int): List<Festivita> {
        cachePerAnno[anno]?.let { return it }

        return try {
            val risposta = api.getFestivita(anno)
            val festivita = risposta.mapNotNull { dto ->
                try {
                    Festivita(data = LocalDate.parse(dto.data), nome = dto.nomeLocale)
                } catch (e: Exception) {
                    null // data in formato inatteso: la scartiamo invece di far crashare l'app
                }
            }
            cachePerAnno[anno] = festivita
            festivita
        } catch (e: Exception) {
            // Nessuna connessione o API non raggiungibile: nessuna festività da mostrare,
            // ma il calendario resta comunque utilizzabile (fallback silenzioso).
            emptyList()
        }
    }
}
