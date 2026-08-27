package com.silvianikikarim.studentassistant.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * ZenQuotes espone la "frase del giorno" (uguale per tutti, cambia una volta al giorno)
 * su questo endpoint. Non richiede API key, ma chiede esplicitamente di NON richiamarlo
 * più volte al giorno: per questo il risultato va sempre passato attraverso la cache
 * (vedi FrasedelGiornoCache) prima di essere richiesto di nuovo.
 */
interface ZenQuotesApi {

    @GET("api/today")
    suspend fun getFraseDelGiorno(): List<FraseDto>

    companion object {
        private const val BASE_URL = "https://zenquotes.io/"

        fun create(): ZenQuotesApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ZenQuotesApi::class.java)
        }
    }
}
