package com.copy.kiascreen.room

import androidx.room.TypeConverter
import java.util.Date

class DateConverts {

    @TypeConverter
    fun fromDate(value : Date?) : Long? {
        return value?.let { value.time }
    }

    @TypeConverter
    fun fromLong(date : Long?) : Date? {
        return date?.let { Date(date) }
    }
}