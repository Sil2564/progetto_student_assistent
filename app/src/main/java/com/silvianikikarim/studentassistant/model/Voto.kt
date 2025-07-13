package com.silvianikikarim.studentassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voti")
data class Voto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materia: String,
    val voto: Int,
    val data: String,
    val descrizione: String,
    val note: String
)
