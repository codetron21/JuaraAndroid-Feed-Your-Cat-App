package com.codetron.feedyourcat.database

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun dateToTime(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun timeToDate(time: Long): Date {
        return Date(time)
    }

    @TypeConverter
    fun uriToString(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun stringToUri(str: String): Uri {
        return Uri.parse(str)
    }

}