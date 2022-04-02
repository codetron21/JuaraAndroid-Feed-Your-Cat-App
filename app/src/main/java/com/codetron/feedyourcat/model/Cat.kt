package com.codetron.feedyourcat.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "Cat")
data class Cat(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "photo")
    val photo: String,
    @ColumnInfo(name = "birth_date")
    val birth_date: Date
) : Parcelable {

    constructor(name: String, photo: String, dateAdded: Date) : this(0, name, photo, dateAdded)

}