package com.leadinsource.dailyweightlog.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * Created by Matt on 21/02/2018.
 */
@Database(entities = {Weight.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class WeightDatabase extends RoomDatabase {
    public abstract WeightDao weightDao();

}
