package com.leadinsource.dailyweightlog;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.leadinsource.dailyweightlog.databinding.ActivityMainBinding;
import com.leadinsource.dailyweightlog.db.DataContract;
import com.leadinsource.dailyweightlog.utils.ReminderUtils;
import com.leadinsource.dailyweightlog.utils.Units;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CHART_LOADER_ID = 0;
    private static final int PREVIOUS_WEIGHT_LOADER_ID = 1;
    private static final int TODAY_WEIGHT_LOADER_ID = 2;
    private static final int CHECK_TODAY_WEIGHT_LOADER_ID = 3;

    private static final String TAG = "Main Activity";
    public static final int SETTINGS_REQUEST_CODE = 15;
    public static final String PREFERENCES_CHANGED = "changed_prefs";

    private boolean weightEnteredToday = false;

    private Uri lastInsertedUri;
    private WeightAdapter todayWeightAdapter;

    ActivityMainBinding binding;
    private WeightAdapter previousWeightAdapter;

    //LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.layoutPrevious.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        binding.todayLayout.btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportLoaderManager().destroyLoader(TODAY_WEIGHT_LOADER_ID);
                getSupportLoaderManager().destroyLoader(CHART_LOADER_ID);
                getSupportLoaderManager().restartLoader(CHART_LOADER_ID, null, MainActivity.this);
                deleteWeight();
                displayWeightInputUI();
            }
        });

        binding.todayLayout.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mimeType = "text/plain";
                String title = "Share your today's weight";
                float weight;


                if (binding.inputLayout.etTodayWeight.getText().length() == 0) {
                    return;
                }

                try {
                    weight = Float.parseFloat(binding.inputLayout.etTodayWeight.getText().toString());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    return;
                }

                String textToShare = "Hello! Today I weigh only " + Units.getWeightTextWithUnits(weight);

                ShareCompat.IntentBuilder.from(MainActivity.this)
                        .setChooserTitle(title)
                        .setType(mimeType)
                        .setText(textToShare)
                        .startChooser();

            }
        });

        //chart = findViewById(R.id.chart);

        getSupportLoaderManager().initLoader(CHART_LOADER_ID, null, this);

        getSupportLoaderManager().initLoader(CHECK_TODAY_WEIGHT_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
        }

        return true;
    }


    public void addWeight(View view) {

        float weight, fatPc;

        if (binding.inputLayout.etTodayWeight.getText().length() == 0) {
            return;
        }

        try {
            weight = Float.parseFloat(binding.inputLayout.etTodayWeight.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return;
        }

        ContentValues values = new ContentValues();

        values.put(DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG, weight);
        if (binding.inputLayout.etFatPc.getText().length() > 0) {

            try {
                fatPc = Float.parseFloat(binding.inputLayout.etFatPc.getText().toString());
                values.put(DataContract.WeightEntry.COLUMN_FAT_PC, fatPc);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        Uri uri = getContentResolver().insert(DataContract.WeightEntry.CONTENT_URI, values);
        if (uri != null) {
            lastInsertedUri = uri;
        }

        getSupportLoaderManager().restartLoader(CHART_LOADER_ID, null, this);
        displayWeightAddedUI();
        ReminderUtils.scheduleWeightReminder(this);
    }

    private void displayWeightAddedUI() {
        ConstraintLayout inputLayout = findViewById(R.id.inputLayout);
        inputLayout.setVisibility(View.INVISIBLE);
        ConstraintLayout todayLayout = findViewById(R.id.todayLayout);
        todayLayout.setVisibility(View.VISIBLE);
        getSupportLoaderManager().initLoader(TODAY_WEIGHT_LOADER_ID, null, this);
    }

    private void displayWeightInputUI() {
        ConstraintLayout todayLayout = findViewById(R.id.todayLayout);
        todayLayout.setVisibility(View.INVISIBLE);
        ConstraintLayout inputLayout = findViewById(R.id.inputLayout);
        inputLayout.setVisibility(View.VISIBLE);
        binding.inputLayout.etTodayWeight.setText("");
        binding.inputLayout.etFatPc.setText("");
    }

    void deleteWeight() {
        getContentResolver().delete(lastInsertedUri, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new WeightLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int loaderId = loader.getId();

        if (loaderId == CHART_LOADER_ID) {
            if (data.getCount() > 0) {
                setUpChart(data);
            }
            return;
        }

        if (loaderId == CHECK_TODAY_WEIGHT_LOADER_ID) {
            weightEnteredToday = weightEnteredToday(data);
            if (weightEnteredToday) {
                data.moveToFirst();
                long id = data.getLong(data.getColumnIndex(DataContract.WeightEntry._ID));
                lastInsertedUri = DataContract.WeightEntry.CONTENT_URI.buildUpon().appendPath("" + id).build();
                getSupportLoaderManager().initLoader(PREVIOUS_WEIGHT_LOADER_ID, null, this);
                displayWeightAddedUI();
            } else {
                getSupportLoaderManager().initLoader(PREVIOUS_WEIGHT_LOADER_ID, null, this);
                displayWeightInputUI();
            }
            return;
        }

        if (loaderId == PREVIOUS_WEIGHT_LOADER_ID) {
            setUpPreviousWeights(data);
        } else {
            setUpTodayWeights(data);
        }

    }

    private boolean weightEnteredToday(Cursor data) {
        if (data.moveToFirst()) {
            String date = data.getString(data.getColumnIndex(DataContract.WeightEntry.COLUMN_DATE));

            Timestamp timestamp = Timestamp.valueOf(date);

            Date savedDate = new Date(timestamp.getTime());

            Date currentDay = Units.stripTime(new Date(System.currentTimeMillis()));

            return savedDate.after(currentDay);
        } else {
            return false;
        }
    }

    private void setUpTodayWeights(Cursor data) {
        if (todayWeightAdapter == null) {
            todayWeightAdapter = new WeightAdapter(data, 1);
            binding.todayLayout.rvToday.setAdapter(todayWeightAdapter);
            binding.todayLayout.rvToday.setLayoutManager(new LinearLayoutManager(this));
            binding.todayLayout.rvToday.setHasFixedSize(true);
        } else {
            binding.todayLayout.rvToday.setAdapter(todayWeightAdapter);
            todayWeightAdapter.updateData(data);
            todayWeightAdapter.notifyDataSetChanged();
        }
    }

    private void setUpPreviousWeights(Cursor data) {

        if (weightEnteredToday) {
            previousWeightAdapter = new WeightAdapter(data, WeightAdapter.PREVIOUS_NO_TODAY);
        } else {
            previousWeightAdapter = new WeightAdapter(data, WeightAdapter.PREVIOUS);
        }

        binding.layoutPrevious.rvPrevious.setAdapter(previousWeightAdapter);
        binding.layoutPrevious.rvPrevious.setLayoutManager(new LinearLayoutManager(this));
        binding.layoutPrevious.rvPrevious.setHasFixedSize(true);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //setUpPreviousWeights(null);
    }

    private void setUpChart(Cursor data) {

        ArrayList<Entry> entries = new ArrayList<>();

        data.moveToFirst();
        while (!data.isAfterLast()) {
            long id = data.getLong(data.getColumnIndex(DataContract.WeightEntry._ID));
            float weight = data.getFloat(data.getColumnIndex(DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG));
            Log.d("Chart", "adding " + id + " / " + weight);
            entries.add(new Entry(id, weight));
            data.moveToNext();
        }

        Collections.reverse(entries);
        LineDataSet dataSet = new LineDataSet(entries, "Weight in time");
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        LineData lineData = new LineData(dataSet);

        binding.chart.setData(lineData);
        binding.chart.setDrawGridBackground(false);
        binding.chart.setDescription(null);
        YAxis yAxis = binding.chart.getAxisRight();
        yAxis.setEnabled(false);
        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setEnabled(false);
        binding.chart.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==SETTINGS_REQUEST_CODE) {
            boolean changed = data.getBooleanExtra(PREFERENCES_CHANGED, false);
            if(changed) {
                todayWeightAdapter.notifyDataSetChanged();
                previousWeightAdapter.notifyDataSetChanged();
            }
        }
    }
}