package com.leadinsource.dailyweightlog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.leadinsource.dailyweightlog.utils.Units;


/**
 * Provides a list of settings, defined in preferences.xml.
 *
 * Also implements listeners responsible for setting the summary for some of those settings and
 * checks if the value of the height in EditTextPreference is in order.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        Preference preference = findPreference(getString(R.string.pref_height_key));
        preference.setOnPreferenceChangeListener(this);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();


        // to set preference summary on all but CheckBoxPreference (which handles its summary)
        int count = preferenceScreen.getPreferenceCount();

        for(int i=0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if(!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }

        }
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if(preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex>=0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        if(preference instanceof EditTextPreference) {
            preference.setSummary(Units.getHeightTextWithUnits(value));
            //EditTextPreference etPreference = (EditTextPreference) preference;
            //String value = etPreference.getText().toString();

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(!(preference instanceof CheckBoxPreference)) {
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceSummary(preference, value);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        // destroy
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast error = Toast.makeText(getContext(), "Please select a correct height", Toast.LENGTH_SHORT);

        String heightKey = getString(R.string.pref_height_key);
        if(preference.getKey().equals(heightKey)) {

            String stringHeight = ((String) (newValue)).trim();
            try {
                float height = Float.parseFloat(stringHeight);
                if(height > 270 || height < 10) {
                    error.show();
                    return false;
                }
            } catch (NumberFormatException nfe) {
                error.show();
                return false;
            }
        }
        return true;
    }
}
