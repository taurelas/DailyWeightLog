package com.olibei.dailyweightlog.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.util.Date

/**
 * Created by Matt on 23/02/2018.
 */

const val _ID = "_id"
const val TABLE_NAME = "weights"
const val COLUMN_DATE = "date"
const val COLUMN_WEIGHT_IN_KG = "weight_in_kg"
const val COLUMN_FAT_PC = "fat_pc"

@Entity
data class Weight(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    var id: Int = 0,

    @ColumnInfo(name = COLUMN_DATE)
    var date: Date,

    @ColumnInfo(name = COLUMN_WEIGHT_IN_KG)
    var weightInKg: Float = 0.0f,

    @ColumnInfo(name = COLUMN_FAT_PC)
    var fatPc: Float? = null,
)
