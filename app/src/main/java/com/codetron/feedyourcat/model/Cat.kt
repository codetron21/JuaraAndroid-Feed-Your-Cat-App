package com.codetron.feedyourcat.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "Cat")
data class Cat(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "photo")
    val photo: Uri,
    @ColumnInfo(name = "birth_date")
    val birthDate: Date
) : Parcelable {

    constructor(name: String, photo: Uri, birthDate: Date) : this(0, name, photo, birthDate)

}