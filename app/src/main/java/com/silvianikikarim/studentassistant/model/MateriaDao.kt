package com.silvianikikarim.studentassistant.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMateria(materia: Materia): Long

    @Delete
    suspend fun deleteMateria(materia: Materia)

    @Query("SELECT * FROM materie ORDER BY nome ASC")
    fun getAllMaterie(): Flow<List<Materia>>

    @Query("SELECT * FROM materie WHERE LOWER(nome) = LOWER(:nome) LIMIT 1")
    suspend fun getByNomeIgnoreCase(nome: String): Materia?
}
