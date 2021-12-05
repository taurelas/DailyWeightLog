/* package com.leadinsource.dailyweightlog

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceFragment
import androidx.appcompat..CheckBoxPreference
import androidx.appcompat.preference.EditTextPreference
import androidx.appcompat.preference.ListPreference
import androidx.appcompat.preference.Preference
import androidx.appcompat.preference.PreferenceFragmentCompat
import androidx.appcompat.preference.PreferenceScreen
import android.widget.Toast

import com.leadinsource.dailyweightlog.utils.Units


*//**
 * Provides a list of settings, defined in preferences.xml.
 *
 *
 * Also implements listeners responsible for setting the summary for some of those settings and
 * checks if the value of the height in EditTextPreference is in order.
 *//*

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        addPreferencesFromResource(R.xml.preferences)

        val preference = findPreference(getString(R.string.pref_height_key))
        preference.onPreferenceChangeListener = this

        setHeightPreference()

        val sharedPreferences = preferenceScreen.sharedPreferences
        val preferenceScreen = preferenceScreen


        // to set preference summary on all but CheckBoxPreference (which handles its summary)
        val count = preferenceScreen.preferenceCount

        for (i in 0 until count) {
            val p = preferenceScreen.getPreference(i)
            if (p !is CheckBoxPreference) {
                val value = sharedPreferences.getString(p.key, "")
                setPreferenceSummary(p, value)
            }

        }
    }

    private fun setPreferenceSummary(preference: Preference, value: String) {
        if (preference is ListPreference) {
            val prefIndex = preference.findIndexOfValue(value)
            if (prefIndex >= 0) {
                preference.summary = preference.entries[prefIndex]
            }
        }
        if (preference is EditTextPreference) {
            preference.setSummary(Units.getHeightTextWithUnits(value))
            //EditTextPreference etPreference = (EditTextPreference) preference;
            //String value = etPreference.getText().toString();

        }
    }

    *//**
     * Disables height preference if BMI not used
     *
     * @param sharedPreferences use by setHeightPreferenceSummary
     * @param key carries info which preference changed
     *//*

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {

        if (key == getString(R.string.pref_uses_bmi_key)) {
            setHeightPreference()
        }

        if (key == getString(R.string.pref_height_key)) {
            setHeightPreferenceSummary(sharedPreferences)
        }
    }

    private fun setHeightPreferenceSummary(sharedPreferences: SharedPreferences) {

        val value = sharedPreferences.getString(getString(R.string.pref_height_key), "")
        findPreference(getString(R.string.pref_height_key)).summary = Units.getHeightTextWithUnits(value!!)
    }

    internal fun setHeightPreference() {
        val usesBmiPreference = findPreference(getString(R.string.pref_uses_bmi_key)) as CheckBoxPreference

        if (usesBmiPreference.isChecked) {
            findPreference(getString(R.string.pref_height_key)).isEnabled = true
            if (preferenceScreen.sharedPreferences.getString(getString(R.string.pref_height_key),
                            "") == "") {
                preferenceManager.showDialog(findPreference(getString(R.string.pref_height_key)))
            }

        } else {
            findPreference(getString(R.string.pref_height_key)).isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        // destroy
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val error = Toast.makeText(context, "Please select a correct height", Toast.LENGTH_SHORT)

        val heightKey = getString(R.string.pref_height_key)
        if (preference.key == heightKey) {

            val stringHeight = (newValue as String).trim { it <= ' ' }
            try {
                val height = java.lang.Float.parseFloat(stringHeight)
                if (height > 270 || height < 10) {
                    error.show()
                    return false
                }
            } catch (nfe: NumberFormatException) {
                error.show()
                return false
            }

        }
        return true
    }
} */
