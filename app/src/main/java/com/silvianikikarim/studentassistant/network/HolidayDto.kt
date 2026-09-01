package com.silvianikikarim.studentassistant.network

import com.google.gson.annotations.SerializedName

/**
 * Rappresenta una singola festività così come arriva da Nager.Date.
 * Esempio di risposta reale:
 * [{"date": "2026-01-01", "localName": "Capodanno", "name": "New Year's Day", ...}]
 */
data class HolidayDto(
    @SerializedName("date") val data: String,
    @SerializedName("localName") val nomeLocale: String
)
