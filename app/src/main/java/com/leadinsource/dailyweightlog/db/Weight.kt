package com.leadinsource.dailyweightlog.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomMasterTable.TABLE_NAME

import java.util.Date

/**
 * Created by Matt on 23/02/2018.
 */

const val _ID = "_id"
const val TABLE_NAME = "weights"
const val COLUMN_DATE = "date"
const val COLUMN_WEIGHT_IN_KG = "weight_in_kg"
const val COLUMN_FAT_PC = "fat_pc"

@Entity(tableName = TABLE_NAME)
data class Weight(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = _ID)
    var id: Int = 0,

    @ColumnInfo(name = COLUMN_DATE)
    var date: Date? = null,

    @ColumnInfo(name = COLUMN_WEIGHT_IN_KG)
    var weightInKg: Float = 0.toFloat(),

    @ColumnInfo(name = COLUMN_FAT_PC)
    var fatPc: Float = 0.toFloat(),
)
