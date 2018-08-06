package com.leadinsource.dailyweightlog.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Matt on 23/02/2018.
 */

public class DateConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
