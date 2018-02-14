package com.leadinsource.dailyweightlog;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.leadinsource.dailyweightlog.db.DataContract;

/**
 * AsyncTask loader for loading Weight data
 */

public class WeightLoader extends AsyncTaskLoader<Cursor> {
    private Cursor retCursor = null;
    private ContentResolver contentResolver;
    private String TAG;

    WeightLoader(Context context) {
        super(context);
        contentResolver = context.getContentResolver();
    }

    @Override
    protected void onStartLoading() {
        TAG = "Loader no " + getId();
        Log.d(TAG, "onStartLoading");
        if(retCursor!= null) {
            deliverResult(retCursor);
        } else {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        Log.d(TAG, "loadInBackground");
        try {
            return contentResolver.query(DataContract.WeightEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    DataContract.WeightEntry.COLUMN_DATE + " DESC");
        } catch(Exception e) {
            Log.e("Loader", "Failed to asynchronously load cursor.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(Cursor data) {
        Log.d(TAG, "delivering result");
        retCursor = data;
        super.deliverResult(data);
    }
}
