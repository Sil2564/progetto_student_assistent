package com.silvianikikarim.studentassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materie")
data class Materia(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    /** 1, 2 o 3: l'anno di corso a cui appartiene questa materia. */
    val anno: Int
)
