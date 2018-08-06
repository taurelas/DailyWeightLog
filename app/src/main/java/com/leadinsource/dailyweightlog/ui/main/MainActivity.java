package com.leadinsource.dailyweightlog.ui.main;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leadinsource.dailyweightlog.HistoryActivity;
import com.leadinsource.dailyweightlog.R;
import com.leadinsource.dailyweightlog.SettingsActivity;
import com.leadinsource.dailyweightlog.WeightAdapter;
import com.leadinsource.dailyweightlog.app.DWLApplication;
import com.leadinsource.dailyweightlog.databinding.ActivityMainBinding;
import com.leadinsource.dailyweightlog.db.DataContract;
import com.leadinsource.dailyweightlog.utils.ReminderUtils;
import com.leadinsource.dailyweightlog.utils.Units;

import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    public static final int SETTINGS_REQUEST_CODE = 15;
    public static final String PREFERENCES_CHANGED = "changed_prefs";

    private boolean weightEnteredToday = false;

    private WeightAdapter todayWeightAdapter;

    ActivityMainBinding binding;
    private WeightAdapter previousWeightAdapter;

    @Inject
    SharedPreferences defaultSharedPreferences;

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DWLApplication.app().appComponent().inject(this);
        setTheme(R.style.AppThemeBar);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getWeightEntered().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean weightEntered) {
                if(weightEntered) {
                    displayWeightAddedUI();
                } else {
                    displayWeightInputUI();
                }
            }
        });

        /*binding.previous.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });*/

      /*  binding.todayLayout.btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Undo!", Toast.LENGTH_SHORT).show();
                viewModel.undo();
                displayWeightInputUI();
            }
        });*/

      /*  binding.todayLayout.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Share!", Toast.LENGTH_SHORT).show();
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
        });*/
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

    /**
     * Triggered when add button is pressed
     *
     * @param view reference to the button, not used here.
     */
    public void addWeight(View view) {

        float weight, fatPc = 0.0f;

        EditText etWeight;
        EditText etFatPc;

        if (doesUseFatPc()) {
            etWeight = binding.inputLayout.etTodayWeight;
            etFatPc = binding.inputLayout.etFatPc;
        } else {
            etWeight = binding.inputNoFat.etTodayWeight;
            etFatPc = null;
        }
        // exit if no weight
        if (etWeight.getText().length() == 0) {
            return;
        }

        // set error and exit if weight is wrong
        try {
            weight = Float.parseFloat(etWeight.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            etWeight.setError("Enter correct weight");
            return;
        }

        // set error and exit if fat % is wrong
        if (etFatPc != null) {
            try {
                fatPc = Float.parseFloat(etFatPc.getText().toString());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                etFatPc.setError("Enter correct fat percentage");
                return;
            }
        }

        viewModel.addData(weight, fatPc);


    }

    private void displayWeightAddedUI() {
        binding.inputLayout.getRoot().setVisibility(View.INVISIBLE);
        binding.inputNoFat.getRoot().setVisibility(View.INVISIBLE);
        binding.todayLayout.getRoot().setVisibility(View.VISIBLE);

        // we put this here because only when stuff is added we need to schedule another one
        ReminderUtils.scheduleWeightReminder(this);
    }

    private void displayWeightInputUI() {
        binding.todayLayout.getRoot().setVisibility(View.INVISIBLE);

        if (doesUseFatPc()) {
            binding.inputNoFat.getRoot().setVisibility(View.INVISIBLE);
            binding.inputLayout.getRoot().setVisibility(View.VISIBLE);
            binding.inputLayout.etTodayWeight.setText("");
            binding.inputLayout.etFatPc.setText("");
        } else {
            binding.inputLayout.getRoot().setVisibility(View.INVISIBLE);
            binding.inputNoFat.getRoot().setVisibility(View.VISIBLE);
        }

        //TODO unschedule any reminders


    }

    private boolean doesUseFatPc() {
        return defaultSharedPreferences
                .getBoolean(getString(R.string.pref_uses_fat_pc_key),
                        getResources().getBoolean(R.bool.pref_uses_fat_pc_default));
    }

    private boolean doesUseBMI() {
        return defaultSharedPreferences
                .getBoolean(getString(R.string.pref_uses_bmi_key),
                        getResources().getBoolean(R.bool.pref_uses_bmi_default));
    }

    /**
     * Checks if the date's day in top element in the cursor is not before today.
     * <p>
     * It does not cater for any mischief such as changing the system time.
     *
     * @param data - Cursor with weights to get the first one
     * @return true if the weight was entered today already, otherwise false
     */
    private boolean weightEnteredToday(Cursor data) {
        if (data.moveToFirst()) {
            String date = data.getString(data.getColumnIndex(DataContract.WeightEntry.COLUMN_DATE));
            Timestamp timestamp = Timestamp.valueOf(date);

            Date savedDate = new Date(timestamp.getTime());

            Date savedDay = Units.stripTime(savedDate);

            Date currentDay = Units.stripTime(new Date(System.currentTimeMillis()));
            // savedDate or saveDay?
            return !savedDay.before(currentDay);
        } else {
            return false;
        }
    }

    private void setUpTodayWeights(Cursor data) {
        if (todayWeightAdapter == null) {
            todayWeightAdapter = new WeightAdapter(data, 1, defaultSharedPreferences);
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
            previousWeightAdapter = new WeightAdapter(data, WeightAdapter.PREVIOUS_NO_TODAY, defaultSharedPreferences);
        } else {
            previousWeightAdapter = new WeightAdapter(data, WeightAdapter.PREVIOUS, defaultSharedPreferences);
        }

        binding.layoutPrevious.rvPrevious.setAdapter(previousWeightAdapter);
        binding.layoutPrevious.rvPrevious.setLayoutManager(new LinearLayoutManager(this));
        binding.layoutPrevious.rvPrevious.setHasFixedSize(true);
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE) {
            boolean changed = data.getBooleanExtra(PREFERENCES_CHANGED, false);
            if (changed) {
                // this is a fallout from using PreferenceFragment in SettingsActivityas we can't
                // custimise it to cater for this scenario
                if (doesUseBMI() && isHeightEmpty()) {
                    setNoBMI();
                }
                if (todayWeightAdapter != null) {
                    Log.d(TAG, "todayWeightAdapter is not null");
                    todayWeightAdapter.notifyColumnsChanged();
                } else {
                    Log.d(TAG, "todayWeightAdapter is null");
                }
                previousWeightAdapter.notifyColumnsChanged();
                if (binding.inputLayout.getRoot().getVisibility() == View.VISIBLE ||
                        binding.inputNoFat.getRoot().getVisibility() == View.VISIBLE) {
                    displayWeightInputUI();
                }
            }
        }
    }

    /**
     * We manually switch the BMI off as no height.
     */

    @SuppressLint("ApplySharedPref")
    private void setNoBMI() {
        defaultSharedPreferences
                .edit()
                .putBoolean(getString(R.string.pref_uses_bmi_key), false)
                .commit();
    }

    private boolean isHeightEmpty() {
        return defaultSharedPreferences
                .getString(getString(R.string.pref_height_key),
                        getString(R.string.pref_height_value_default))
                .isEmpty();

    }
}