package com.silvianikikarim.studentassistant.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Ogni Nota è di UN solo tipo (scelto al momento della creazione dal BottomSheet
 * "Aggiungi appunto"). Non ci sono note miste testo+immagine+pdf: questo semplifica
 * molto lo schema Room (niente tabelle di relazione per allegati multipli).
 */
enum class TipoNota {
    TESTO,
    IMMAGINE,
    PDF
}

@Entity(
    tableName = "note",
    foreignKeys = [
        ForeignKey(
            entity = Materia::class,
            parentColumns = ["id"],
            childColumns = ["materiaId"],
            onDelete = ForeignKey.CASCADE // se elimino la materia, elimino anche le sue note
        )
    ],
    indices = [Index("materiaId")]
)
data class Nota(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val materiaId: Long,
    val tipo: TipoNota,
    val titolo: String,

    // Usato solo se tipo == TESTO
    val testo: String? = null,

    // Usato solo se tipo == IMMAGINE o PDF.
    // È il path assoluto del file COPIATO nello storage interno dell'app
    // (non un content:// Uri, che potrebbe diventare invalido).
    val percorsoFile: String? = null,

    val dataCreazione: Long = System.currentTimeMillis(),
    val dataModifica: Long = System.currentTimeMillis()
)
