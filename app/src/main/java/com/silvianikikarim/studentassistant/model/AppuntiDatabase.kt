package com.silvianikikarim.studentassistant.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Materia::class, Nota::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppuntiDatabase : RoomDatabase() {

    abstract fun materiaDao(): MateriaDao
    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile
        private var INSTANCE: AppuntiDatabase? = null

        fun getDatabase(context: Context): AppuntiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppuntiDatabase::class.java,
                    "appunti_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
