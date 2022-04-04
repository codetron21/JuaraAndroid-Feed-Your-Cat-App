package com.codetron.feedyourcat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.codetron.feedyourcat.model.Cat
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedCatDao {

//    @Transaction
//    @Query("SELECT * FROM Cat ORDER BY Cat.name ASC")
//    fun getAllSortByName(): Flow<List<FeedCat>>
//
//    @Transaction
//    @Query("SELECT * FROM Cat ORDER BY Cat.birth_date ASC")
//    fun getAllSortByNewest(): Flow<List<FeedCat>>
//
//    @Transaction
//    @Query("SELECT * FROM Cat ORDER BY Cat.birth_date DESC")
//    fun getAllSortByLatest(): Flow<List<FeedCat>>

    @Transaction
    @Query(
        "SELECT Cat.id, Cat.photo,Cat.birth_date,Cat.name FROM Cat LEFT JOIN Feed ON Cat.id = Feed.cat_id WHERE Feed.cat_id IS NULL"
    )
    fun getAllCatAvailable(): Flow<List<Cat>>

}