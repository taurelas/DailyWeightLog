package com.leadinsource.dailyweightlog.ui.welcome

import android.content.SharedPreferences

/**
 * Responsible for obtaining and saving the app's first run status
 */

const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"

internal class FirstRunManager(var defaultSharedPreferences: SharedPreferences) {

    val isFirstTimeLaunch: Boolean
        get() = defaultSharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true)

    fun setFirstTimeLaunched() {
        val editor = defaultSharedPreferences.edit()
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, false)
        editor.apply()
    }
}
