package com.silvianikikarim.studentassistant.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Voto::class], version = 1, exportSchema = false)
abstract class VotoDatabase : RoomDatabase() {

    abstract fun votoDao(): VotoDao

    companion object {
        @Volatile
        private var INSTANCE: VotoDatabase? = null

        fun getDatabase(context: Context): VotoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VotoDatabase::class.java,
                    "voto_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
