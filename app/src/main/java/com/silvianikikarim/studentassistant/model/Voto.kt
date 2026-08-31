package com.silvianikikarim.studentassistant.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "voti",
    foreignKeys = [
        ForeignKey(
            entity = Materia::class,
            parentColumns = ["id"],
            childColumns = ["materiaId"],
            onDelete = ForeignKey.CASCADE // se elimino la materia, elimino anche i suoi voti (stesso comportamento di Nota)
        )
    ],
    indices = [Index("materiaId")]
)
data class Voto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val materiaId: Long,
    val voto: Int,
    val data: String,
    val descrizione: String,
    val note: String
)
