package com.leadinsource.dailyweightlog.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for the project's main database
 */

public class DataContract {
    private DataContract() {
    }
    // for content provider
    public static final String AUTHORITY = "com.leadinsource.dailyweightlog";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_WEIGHTS = "weights";

    // sql database structure + URI for that
    public static class WeightEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEIGHTS).build();
        public static final String TABLE_NAME = "weights";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEIGHT_IN_KG = "weight_in_kg";
        public static final String COLUMN_FAT_PC = "fat_pc";
    }
}
