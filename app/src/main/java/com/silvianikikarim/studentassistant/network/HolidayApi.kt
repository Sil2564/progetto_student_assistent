package com.silvianikikarim.studentassistant.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Nager.Date espone le festività pubbliche di un paese per un dato anno.
 * API pubblica, gratuita, senza API key.
 * Endpoint: https://date.nager.at/api/v3/PublicHolidays/{anno}/{codicePaese}
 */
interface HolidayApi {

    @GET("api/v3/PublicHolidays/{anno}/{codicePaese}")
    suspend fun getFestivita(
        @Path("anno") anno: Int,
        @Path("codicePaese") codicePaese: String = "IT"
    ): List<HolidayDto>

    companion object {
        private const val BASE_URL = "https://date.nager.at/"

        fun create(): HolidayApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HolidayApi::class.java)
        }
    }
}
