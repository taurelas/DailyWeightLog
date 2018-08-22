package com.leadinsource.dailyweightlog.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

/**
 * Created by Matt on 21/02/2018.
 */
@Database(entities = arrayOf(Weight::class), version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class WeightDatabase : RoomDatabase() {
    abstract fun weightDao(): WeightDao

}
