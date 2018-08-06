package com.leadinsource.dailyweightlog.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Matt on 23/02/2018.
 */
@Entity(tableName = DataContract.WeightEntry.TABLE_NAME)
public class Weight {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DataContract.WeightEntry._ID)
    private int id;

    @ColumnInfo(name = DataContract.WeightEntry.COLUMN_DATE)
    private Date date;

    @NonNull
    @ColumnInfo(name = DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG)
    private float weightInKg;

    @ColumnInfo(name = DataContract.WeightEntry.COLUMN_FAT_PC)
    private float fatPc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getWeightInKg() {
        return weightInKg;
    }

    public void setWeightInKg(float weightInKg) {
        this.weightInKg = weightInKg;
    }

    public float getFatPc() {
        return fatPc;
    }

    public void setFatPc(float fatPc) {
        this.fatPc = fatPc;
    }



}
