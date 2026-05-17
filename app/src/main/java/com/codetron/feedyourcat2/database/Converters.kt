package com.codetron.feedyourcat2.database

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*
import androidx.core.net.toUri

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
        return str.toUri()
    }

    @TypeConverter
    fun timesToString(times: List<Int>): String {
        return times.joinToString(",")
    }

    @TypeConverter
    fun stringToTimes(str: String): List<Int> {
        return str.split(",").map(String::toInt)
    }

}