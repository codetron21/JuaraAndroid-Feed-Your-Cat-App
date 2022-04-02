package com.codetron.feedyourcat.database

import androidx.room.*
import com.codetron.feedyourcat.model.Cat
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {

    @Query("SELECT * FROM cat ORDER BY birth_date ASC")
    fun getAll(): Flow<List<Cat>>

    @Query("SELECT * FROM cat WHERE id = :id")
    suspend fun getById(id: Long): Cat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cat: Cat): Long

    @Query("DELETE FROM cat WHERE id = :id")
    suspend fun deleteById(id: Long)

}