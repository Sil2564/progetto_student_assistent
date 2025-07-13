package com.silvianikikarim.studentassistant.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoto(voto: Voto)

    @Delete
    suspend fun deleteVoto(voto: Voto)

    @Query("SELECT * FROM voti ORDER BY data DESC")
    fun getAllVoti(): Flow<List<Voto>>
}
