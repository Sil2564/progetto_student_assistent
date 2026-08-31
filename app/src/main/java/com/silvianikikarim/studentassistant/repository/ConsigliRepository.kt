package com.silvianikikarim.studentassistant.repository

import com.silvianikikarim.studentassistant.model.ArticoloConsiglio
import com.silvianikikarim.studentassistant.model.ArticoliConsigli
import com.silvianikikarim.studentassistant.model.FraseDelGiorno
import com.silvianikikarim.studentassistant.network.ZenQuotesApi
import com.silvianikikarim.studentassistant.util.FrasedelGiornoCache

class ConsigliRepository(
    private val api: ZenQuotesApi,
    private val cache: FrasedelGiornoCache
) {

    val articoli: List<ArticoloConsiglio> = ArticoliConsigli.lista

    /**
     * Restituisce la frase del giorno seguendo questo ordine:
     * 1. Se in cache c'è già la frase di oggi, la usa (nessuna chiamata di rete).
     * 2. Altrimenti prova a scaricarla da ZenQuotes e la salva in cache.
     * 3. Se la rete fallisce (no internet, timeout...), usa l'ultima frase salvata
     *    come fallback, invece di mostrare un errore all'utente.
     */
    suspend fun getFraseDelGiorno(): FraseDelGiorno? {
        cache.leggiSeDiOggi()?.let { return it }

        return try {
            val risposta = api.getFraseDelGiorno()
            val fraseScaricata = risposta.firstOrNull()
                ?.let { FraseDelGiorno(testo = it.testo, autore = it.autore) }

            fraseScaricata?.also { cache.salva(it) }
                ?: cache.leggiUltimaSalvata()
        } catch (e: Exception) {
            cache.leggiUltimaSalvata()
        }
    }
}
