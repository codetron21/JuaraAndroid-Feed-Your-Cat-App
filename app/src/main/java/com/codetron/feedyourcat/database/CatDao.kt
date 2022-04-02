package com.codetron.feedyourcat.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codetron.feedyourcat.model.Cat
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {

    @Query("SELECT * FROM cat ORDER BY birth_date DESC")
    fun getAllSortByNewest(): Flow<List<Cat>>

    @Query("SELECT * FROM cat ORDER BY birth_date ASC")
    fun getAllSortByLatest(): Flow<List<Cat>>

    @Query("SELECT * FROM cat ORDER BY name ASC")
    fun getAllSortByName(): Flow<List<Cat>>

    @Query("SELECT * FROM cat WHERE id = :id")
    suspend fun getById(id: Long): Cat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cat: Cat): Long

    @Query("DELETE FROM cat WHERE id = :id")
    suspend fun deleteById(id: Long)

}