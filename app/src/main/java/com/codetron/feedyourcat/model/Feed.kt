package com.codetron.feedyourcat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Feed")
data class Feed(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "times")
    val times: List<Long>,
    @ColumnInfo(name = "cat_id")
    val catId: Long
)