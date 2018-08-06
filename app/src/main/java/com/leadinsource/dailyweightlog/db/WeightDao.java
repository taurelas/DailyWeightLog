package com.leadinsource.dailyweightlog.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

/**
 * Created by Matt on 23/02/2018.
 */

@Dao
public interface WeightDao {
    @Query("SELECT * FROM "+ DataContract.WeightEntry.TABLE_NAME + " ORDER BY "+ DataContract.WeightEntry.COLUMN_DATE)
    Cursor getAll();

    @Query("SELECT * FROM "+DataContract.WeightEntry.TABLE_NAME + " WHERE "+ DataContract.WeightEntry._ID +" = :weightId")
    LiveData<Weight> getSingleWeight(int weightId);

}
