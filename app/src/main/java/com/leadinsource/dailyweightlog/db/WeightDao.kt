package com.leadinsource.dailyweightlog.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.database.Cursor

/**
 * Created by Matt on 23/02/2018.
 */

@Dao
interface WeightDao {
    @get:Query("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_DATE")
    val all: Cursor

    @Query("SELECT * FROM $TABLE_NAME WHERE $_ID = :weightId")
    fun getSingleWeight(weightId: Int): LiveData<Weight>

}
