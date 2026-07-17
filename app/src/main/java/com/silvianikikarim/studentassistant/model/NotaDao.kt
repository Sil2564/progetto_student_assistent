package com.silvianikikarim.studentassistant.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNota(nota: Nota): Long

    @Update
    suspend fun updateNota(nota: Nota)

    @Delete
    suspend fun deleteNota(nota: Nota)

    @Query("SELECT * FROM note WHERE materiaId = :materiaId ORDER BY dataModifica DESC")
    fun getNoteByMateria(materiaId: Long): Flow<List<Nota>>

    @Query("SELECT * FROM note WHERE id = :notaId")
    suspend fun getNotaById(notaId: Long): Nota?
}
