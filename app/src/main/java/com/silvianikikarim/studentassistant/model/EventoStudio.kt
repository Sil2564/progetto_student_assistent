package com.silvianikikarim.studentassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventi_studio")
data class EventoStudio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titolo: String,
    val materia: String,
    val data: Long, // timestamp
    val oraInizio: String,
    val oraFine: String,
    val tipo: String // STUDIO / ESAME
)
