package com.leadinsource.dailyweightlog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Implementation of SQLiteOpenHelper, creates & upgrades the project's database
 * provides readable or writeable instances of the database.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weights.db";

    public static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEIGHTS_TABLE = "CREATE TABLE " +
                DataContract.WeightEntry.TABLE_NAME + " (" +
                DataContract.WeightEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.WeightEntry.COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG + " REAL NOT NULL, " +
                DataContract.WeightEntry.COLUMN_FAT_PC + " REAL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WEIGHTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.WeightEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
