package com.leadinsource.dailyweightlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Responsible for obtaining and saving the app's first run status
 */

class FirstRunManager {
    private SharedPreferences pref;

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    FirstRunManager(Context context) {
       pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    void setFirstTimeLaunched() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
        editor.apply();
    }

    boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
