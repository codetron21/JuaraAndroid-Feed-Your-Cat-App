package com.codetron.feedyourcat.database

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

}