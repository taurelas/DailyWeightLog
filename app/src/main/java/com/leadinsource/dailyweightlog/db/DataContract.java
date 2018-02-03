package com.leadinsource.dailyweightlog.db;

import android.provider.BaseColumns;

/**
 * Contract for the project's main database
 */

public class DataContract {
    private DataContract() {
    }

    public static class WeightEntry implements BaseColumns {
        public static final String TABLE_NAME = "weights";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_WEIGHT_IN_KG = "weight_in_kg";
        public static final String COLUMN_FAT_PC = "fat_pc";
    }
}
