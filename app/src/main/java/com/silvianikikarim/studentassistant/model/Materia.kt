package com.silvianikikarim.studentassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materie")
data class Materia(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String
)
