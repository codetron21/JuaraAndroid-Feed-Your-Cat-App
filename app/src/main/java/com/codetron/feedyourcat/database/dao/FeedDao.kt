package com.codetron.feedyourcat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codetron.feedyourcat.model.Feed
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedDao {

    @Query("SELECT * FROM Feed")
    fun getAll(): Flow<List<Feed>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(feed: Feed): Long

    @Query("DELETE FROM Feed WHERE id = :id")
    fun deleteById(id: Long)
}