package com.codetron.feedyourcat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Feed")
data class Feed(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private val id: Long,
    @ColumnInfo(name = "times")
    private val times: Long,
    @ColumnInfo(name = "cat_id")
    private val catId: Long
)