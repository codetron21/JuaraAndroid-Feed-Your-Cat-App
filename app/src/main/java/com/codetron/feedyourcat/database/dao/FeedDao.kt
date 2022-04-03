package com.codetron.feedyourcat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codetron.feedyourcat.model.Feed

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(feed: Feed): Long

    @Query("DELETE FROM Feed WHERE id = :id")
    fun deleteById(id: Long)
}