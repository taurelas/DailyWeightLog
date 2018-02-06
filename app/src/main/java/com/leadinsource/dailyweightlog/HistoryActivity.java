package com.leadinsource.dailyweightlog;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.leadinsource.dailyweightlog.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WEIGHT_LOADER_ID = 0;

    ActivityHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);

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

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
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
