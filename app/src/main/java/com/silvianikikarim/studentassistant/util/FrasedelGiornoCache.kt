package com.silvianikikarim.studentassistant.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.silvianikikarim.studentassistant.model.FraseDelGiorno
import kotlinx.coroutines.flow.first
import java.time.LocalDate

private val Context.consigliDataStore by preferencesDataStore(name = "consigli")

/**
 * Salva la frase motivazionale scaricata insieme alla data in cui è stata presa.
 * Serve per non richiamare la API ogni volta che l'utente apre la sezione,
 * ma solo la prima volta in un nuovo giorno (rispettando anche il limite di
 * richieste imposto da ZenQuotes) e per avere comunque una frase da mostrare
 * quando non c'è connessione.
 */
class FrasedelGiornoCache(private val context: Context) {

    companion object {
        private val TESTO_KEY = stringPreferencesKey("frase_testo")
        private val AUTORE_KEY = stringPreferencesKey("frase_autore")
        private val DATA_KEY = stringPreferencesKey("frase_data")
    }

    /** Restituisce la frase salvata solo se risale a oggi, altrimenti null. */
    suspend fun leggiSeDiOggi(): FraseDelGiorno? {
        val preferenze = context.consigliDataStore.data.first()
        val dataSalvata = preferenze[DATA_KEY]
        val oggi = LocalDate.now().toString()

        if (dataSalvata != oggi) return null

        val testo = preferenze[TESTO_KEY] ?: return null
        val autore = preferenze[AUTORE_KEY] ?: return null
        return FraseDelGiorno(testo, autore)
    }

    /** Restituisce l'ultima frase salvata a prescindere dalla data (fallback offline). */
    suspend fun leggiUltimaSalvata(): FraseDelGiorno? {
        val preferenze = context.consigliDataStore.data.first()
        val testo = preferenze[TESTO_KEY] ?: return null
        val autore = preferenze[AUTORE_KEY] ?: return null
        return FraseDelGiorno(testo, autore)
    }

    suspend fun salva(frase: FraseDelGiorno) {
        context.consigliDataStore.edit { preferenze ->
            preferenze[TESTO_KEY] = frase.testo
            preferenze[AUTORE_KEY] = frase.autore
            preferenze[DATA_KEY] = LocalDate.now().toString()
        }
    }
}
