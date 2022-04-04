package com.codetron.feedyourcat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "Feed", foreignKeys = [ForeignKey(
        entity = Cat::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cat_id"),
        onDelete = CASCADE,
    )]
)
data class Feed(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "times")
    val times: List<Int>,
    @ColumnInfo(name = "cat_id")
    val catId: Long
) {
    constructor(times: List<Int>, catId: Long) : this(id = 0, times = times, catId = catId)
}