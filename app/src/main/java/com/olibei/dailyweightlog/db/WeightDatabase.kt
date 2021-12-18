package com.olibei.dailyweightlog.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Created by Matt on 21/02/2018.
 */
@Database(entities = [Weight::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class WeightDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDao

}
