package com.silvianikikarim.studentassistant.network

import com.google.gson.annotations.SerializedName

/**
 * Rappresenta una singola frase così come arriva da ZenQuotes.
 * Esempio di risposta reale: [{"q": "testo della frase", "a": "Autore"}]
 * L'API risponde sempre con un array, anche per l'endpoint "today" (un solo elemento).
 */
data class FraseDto(
    @SerializedName("q") val testo: String,
    @SerializedName("a") val autore: String
)
