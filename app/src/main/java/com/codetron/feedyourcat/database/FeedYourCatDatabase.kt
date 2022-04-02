package com.codetron.feedyourcat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codetron.feedyourcat.model.Cat

@Database(entities = [Cat::class], exportSchema = false, version = 1)
@TypeConverters(value = [Converters::class])
abstract class FeedYourCatDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao

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