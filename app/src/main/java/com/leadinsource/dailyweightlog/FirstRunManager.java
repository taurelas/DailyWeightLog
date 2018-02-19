package com.leadinsource.dailyweightlog;

import android.content.SharedPreferences;

/**
 * Responsible for obtaining and saving the app's first run status
 */

class FirstRunManager {

    SharedPreferences defaultSharedPreferences;

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    FirstRunManager(SharedPreferences defaultSharedPreferences) {
        this.defaultSharedPreferences = defaultSharedPreferences;
    }

    void setFirstTimeLaunched() {
        SharedPreferences.Editor editor = defaultSharedPreferences.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false);
        editor.apply();
    }

    boolean isFirstTimeLaunch() {
        return defaultSharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
