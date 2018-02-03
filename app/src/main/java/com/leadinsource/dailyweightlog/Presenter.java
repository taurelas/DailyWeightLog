package com.leadinsource.dailyweightlog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leadinsource.dailyweightlog.db.DataContract;
import com.leadinsource.dailyweightlog.db.DbHelper;

/**
 * Presenter class that provides data to UI when required
 */

public class Presenter {
    Context context;

    private SQLiteDatabase db;
    private final DbHelper dbHelper;

    public Presenter(Context context) {
        this.context = context;

        dbHelper = new DbHelper(context);
    }

    public long insertData(float weight, float fatPc) {
        db = dbHelper.getWritableDatabase();

        //TODO insert data into

        return 0;
    }

    public Cursor getWeightData() {
        db = dbHelper.getReadableDatabase();

        return db.query(DataContract.WeightEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

}
