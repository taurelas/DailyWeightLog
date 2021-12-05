package com.leadinsource.dailyweightlog

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem

import com.leadinsource.dailyweightlog.app.DWLApplication
import com.leadinsource.dailyweightlog.ui.main.MainActivity

import javax.inject.Inject

private const val TAG = "Settings Activity"

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var isChanged = false


    var defaultSharedPreferences: SharedPreferences? = null
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        DWLApplication.app().appComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()
        defaultSharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        Log.d(TAG, "Reading preferences")
        Log.d(TAG, resources.getString(R.string.pref_uses_fat_pc_key) + ": " +
                defaultSharedPreferences?.getBoolean(resources.getString(R.string.pref_uses_fat_pc_key),
                        resources.getBoolean(R.bool.pref_uses_fat_pc_default)))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                Log.d(TAG, "clicked home button")
                setResult(isChanged)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        isChanged = true
    }

    override fun onPause() {
        super.onPause()
        defaultSharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun setResult(isChanged: Boolean) {
        Log.d(TAG, "setResult called")
        val result = Intent()
        result.putExtra(MainActivity.PREFERENCES_CHANGED, isChanged)
        setResult(Activity.RESULT_OK, result)
    }

    override fun finish() {
        setResult(isChanged)
        super.finish()
    }
}
