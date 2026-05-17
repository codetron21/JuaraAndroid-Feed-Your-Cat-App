package com.codetron.feedyourcat2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codetron.feedyourcat2.database.dao.CatDao
import com.codetron.feedyourcat2.database.dao.FeedCatDao
import com.codetron.feedyourcat2.database.dao.FeedDao
import com.codetron.feedyourcat2.model.Cat
import com.codetron.feedyourcat2.model.Feed

@Database(entities = [Cat::class, Feed::class], exportSchema = false, version = 1)
@TypeConverters(value = [Converters::class])
abstract class FeedYourCatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao
    abstract fun feedDao(): FeedDao
    abstract fun feedCatDao(): FeedCatDao

    companion object {
        @Volatile
        private var INSTANCE: FeedYourCatDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): FeedYourCatDatabase {
            return INSTANCE ?: synchronized(LOCK) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    FeedYourCatDatabase::class.java,
                    "feed_your_cat.db"
                ).build().apply {
                    INSTANCE = this
                }
            }
        }
    }
}