package com.silvianikikarim.studentassistant.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoStudioDao {

    @Query("SELECT * FROM eventi_studio ORDER BY data ASC")
    fun getAllEventi(): Flow<List<EventoStudio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvento(evento: EventoStudio)

    @Delete
    suspend fun deleteEvento(evento: EventoStudio)
}
