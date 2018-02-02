package com.leadinsource.dailyweightlog;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Settings Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Log.d(TAG, "Reading preferences");
        Log.d(TAG, getResources().getString(R.string.pref_fat_pc_key) + ": "+
        sharedPreferences.getBoolean(getResources().getString(R.string.pref_fat_pc_key),
                getResources().getBoolean(R.bool.pref_fat_pc_default)));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Test

        if(key.equals(getString(R.string.pref_fat_pc_key))) {
            Log.d(TAG, getString(R.string.pref_fat_pc_key)+" "+" changed to "+
                    sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.pref_fat_pc_default)));
        }
        if(key.equals(getString(R.string.pref_units_key))) {
            Log.d(TAG, getString(R.string.pref_units_key)+" "+" changed to "+
                    sharedPreferences.getString(key, getResources().getString(R.string.pref_units_value_default)));
        }
        if(key.equals(getString(R.string.pref_sex_key))) {
            Log.d(TAG, getString(R.string.pref_sex_key)+" "+" changed to "+
                    sharedPreferences.getString(key, getResources().getString(R.string.pref_sex_value_default)));
        }
        if(key.equals(getString(R.string.pref_height_key))) {
            Log.d(TAG, getString(R.string.pref_height_key)+" "+" changed to "+
                    sharedPreferences.getString(key, getResources().getString(R.string.pref_height_value_default)));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
