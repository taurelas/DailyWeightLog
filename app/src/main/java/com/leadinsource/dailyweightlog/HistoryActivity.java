package com.leadinsource.dailyweightlog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        WeightAdapter adapter = new WeightAdapter(cursor);

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

}
