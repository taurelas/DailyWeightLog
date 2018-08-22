package com.leadinsource.dailyweightlog.db

import android.arch.persistence.room.TypeConverter

import java.util.Date

/**
 * Created by Matt on 23/02/2018.
 */

object DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}
