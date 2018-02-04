package com.leadinsource.dailyweightlog.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Content Provider for saved Weight data
 */

@SuppressWarnings("ConstantConditions")
public class WeightContentProvider extends ContentProvider {

    public static final int WEIGHTS = 100;
    public static final int WEIGHTS_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_WEIGHTS, WEIGHTS);
        uriMatcher.addURI(DataContract.AUTHORITY, DataContract.PATH_WEIGHTS + "/#", WEIGHTS_WITH_ID);

        return uriMatcher;
    }

    DbHelper dbHelper;

    @Override
    public boolean onCreate() {

        dbHelper = new DbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            case WEIGHTS:
                retCursor = db.query(DataContract.WeightEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case WEIGHTS:
                long id = db.insert(DataContract.WeightEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DataContract.WeightEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.sqlite.SQLiteException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int deleted;

        switch(match) {
            case WEIGHTS_WITH_ID:
                String id = uri.getLastPathSegment();
                String selection = DataContract.WeightEntry._ID + "=?";
                String [] selectionArgs = new String[]{id};
                deleted = db.delete(DataContract.WeightEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        if(deleted>0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
