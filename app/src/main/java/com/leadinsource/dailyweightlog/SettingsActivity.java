package com.leadinsource.dailyweightlog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.leadinsource.dailyweightlog.app.DWLApplication;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Settings Activity";
    private boolean isChanged = false;

    @Inject
    SharedPreferences defaultSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        DWLApplication.app().appComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Log.d(TAG, "Reading preferences");
        Log.d(TAG, getResources().getString(R.string.pref_uses_fat_pc_key) + ": " +
                defaultSharedPreferences.getBoolean(getResources().getString(R.string.pref_uses_fat_pc_key),
                        getResources().getBoolean(R.bool.pref_uses_fat_pc_default)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "clicked home button");
                setResult(isChanged);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        isChanged = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setResult(boolean isChanged) {
        Log.d(TAG, "setResult called");
        Intent result = new Intent();
        result.putExtra(MainActivity.PREFERENCES_CHANGED, isChanged);
        setResult(Activity.RESULT_OK, result);
    }

    @Override
    public void finish() {
        setResult(isChanged);
        super.finish();
    }
}
