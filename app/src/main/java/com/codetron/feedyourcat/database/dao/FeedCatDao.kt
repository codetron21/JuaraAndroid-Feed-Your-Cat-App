package com.codetron.feedyourcat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.codetron.feedyourcat.model.Cat
import com.codetron.feedyourcat.model.FeedCat
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedCatDao {

    @Transaction
    @Query(
        "SELECT c.id as catId, " +
                "f.id as feedId, " +
                "c.name as name, " +
                "c.photo as photo, " +
                "f.times as times " +
                "FROM Feed f JOIN Cat c " +
                "ON c.id = f.cat_id " +
                "ORDER BY c.name ASC"
    )
    fun getAllSortByName(): Flow<List<FeedCat>>

    @Transaction
    @Query(
        "SELECT c.id as catId, " +
                "f.id as feedId, " +
                "c.name as name, " +
                "c.photo as photo, " +
                "f.times as times " +
                "FROM Feed f JOIN Cat c " +
                "ON c.id = f.cat_id " +
                "ORDER BY c.birth_date ASC"
    )
    fun getAllSortByNewest(): Flow<List<FeedCat>>

    @Transaction
    @Query(
        "SELECT c.id as catId, " +
                "f.id as feedId, " +
                "c.name as name, " +
                "c.photo as photo, " +
                "f.times as times " +
                "FROM Feed f JOIN Cat c " +
                "ON c.id = f.cat_id " +
                "ORDER BY c.birth_date DESC"
    )
    fun getAllSortByLatest(): Flow<List<FeedCat>>

    @Transaction
    @Query(
        "SELECT c.id as catId, " +
                "f.id as feedId, " +
                "c.name as name, " +
                "c.photo as photo, " +
                "f.times as times " +
                "FROM Feed f JOIN Cat c " +
                "ON c.id = f.cat_id " +
                "WHERE c.id = :id"
    )
    suspend fun getFeedCatByCatId(id: Long): FeedCat?

    @Transaction
    @Query(
        "SELECT Cat.id, Cat.photo,Cat.birth_date,Cat.name " +
                "FROM Cat LEFT JOIN Feed " +
                "ON Cat.id = Feed.cat_id " +
                "WHERE Feed.cat_id IS NULL"
    )
    fun getAllCatAvailable(): Flow<List<Cat>>

}