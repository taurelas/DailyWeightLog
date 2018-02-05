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
    Cursor retCursor = null;
    ContentResolver contentResolver;


    WeightLoader(Context context) {
        super(context);
        contentResolver = context.getContentResolver();
    }

    @Override
    protected void onStartLoading() {
        Log.d("Loader", "onStartLoading");
        if(retCursor!= null) {
            deliverResult(retCursor);
        } else {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        Log.d("Loader", "loadInBackground");
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
        retCursor = data;
        super.deliverResult(data);
    }
}
