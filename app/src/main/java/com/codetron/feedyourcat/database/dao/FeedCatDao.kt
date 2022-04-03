package com.codetron.feedyourcat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.codetron.feedyourcat.model.FeedCat
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedCatDao {

    @Transaction
    @Query("SELECT * FROM Cat ORDER BY Cat.name ASC")
    fun getAllSortByName(): Flow<List<FeedCat>>

    @Transaction
    @Query("SELECT * FROM Cat ORDER BY Cat.birth_date ASC")
    fun getAllSortByNewest(): Flow<List<FeedCat>>

    @Transaction
    @Query("SELECT * FROM Cat ORDER BY Cat.birth_date DESC")
    fun getAllSortByLatest(): Flow<List<FeedCat>>

    @Query("SELECT COUNT(*) > 0 FROM Feed")
    fun isFeedAvailable(): Flow<Boolean>

    @Query("SELECT COUNT(*) > 0 FROM Cat")
    fun isCatAvailable(): Flow<Boolean>

    @Transaction
    @Query("SELECT COUNT(*) > 0 FROM Cat JOIN Feed ON Feed.cat_id <> Cat.id")
    fun isFeedCatAvailable(): Flow<Boolean>

}