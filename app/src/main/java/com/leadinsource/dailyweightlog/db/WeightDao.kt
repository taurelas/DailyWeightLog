package com.leadinsource.dailyweightlog.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert

/**
 * Created by Matt on 23/02/2018.
 */

@Dao
interface WeightDao {

    @Query("SELECT * FROM weight")
    suspend fun getAll(): List<Weight>

    @Insert
    suspend fun insert(weight: Weight)
/*
    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_DATE")
    fun getAll(): List<Weight>

    @Query("SELECT * FROM $TABLE_NAME WHERE $_ID = :weightId")
    fun getSingleWeight(weightId: Int): List<Weight>*/
}
