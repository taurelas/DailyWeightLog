package com.leadinsource.dailyweightlog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.leadinsource.dailyweightlog.db.DataContract;

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WEIGHT_LOADER_ID = 0;

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rv = findViewById(R.id.recyclerView);

        getSupportLoaderManager().initLoader(WEIGHT_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //re-queries weights
        //getSupportLoaderManager().restartLoader(WEIGHT_LOADER_ID, null, this);
    }

    void setUpRecyclerView(Cursor cursor) {
        HistoryAdapter adapter = new HistoryAdapter(cursor);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new WeightLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        setUpRecyclerView(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        setUpRecyclerView(null);
    }

    static class WeightLoader extends AsyncTaskLoader<Cursor> {

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
                        DataContract.WeightEntry.COLUMN_DATE);
            } catch(Exception e) {
                Log.e("Loader", "Failed to asynchronously load data.");
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
}
