package com.silvianikikarim.studentassistant.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Database unico dell'app. Prima Voto e (Materia+Nota) vivevano in due database
 * Room separati: questo rendeva impossibile avere UNA sola lista di materie
 * condivisa tra Andamento e Appunti (ogni "materia" era solo una stringa libera
 * dentro Voto, scollegata dalla vera tabella Materia usata dagli Appunti).
 *
 * Unendoli in un solo database, Voto può referenziare Materia con una vera
 * foreign key (materiaId), esattamente come già faceva Nota: la tabella
 * "materie" diventa l'unica fonte di verità, usata sia per raggruppare gli
 * appunti sia per il menu a tendina in Andamento.
 */
@Database(
    entities = [Materia::class, Nota::class, Voto::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun materiaDao(): MateriaDao
    abstract fun notaDao(): NotaDao
    abstract fun votoDao(): VotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_assistant_database"
                )
                    // Progetto in sviluppo: se cambia lo schema in futuro, il db
                    // locale viene semplicemente ricreato invece di scrivere
                    // migrazioni manuali. Va bene finché l'app non è in produzione
                    // con dati reali da preservare.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
