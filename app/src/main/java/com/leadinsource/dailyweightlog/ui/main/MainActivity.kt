package com.leadinsource.dailyweightlog.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.leadinsource.dailyweightlog.*
import com.leadinsource.dailyweightlog.app.DWLApplication
import com.leadinsource.dailyweightlog.databinding.ActivityMainBinding
//import com.leadinsource.dailyweightlog.utils.ReminderUtils
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val weightEnteredToday = false

    private var todayWeightAdapter: WeightAdapter? = null

    // DataBinding
    lateinit var binding: ActivityMainBinding

    private var previousWeightAdapter: WeightAdapter? = null

    @Inject
    lateinit var defaultSharedPreferences: SharedPreferences

    private var viewModel: MainActivityViewModel? = null

    private val isHeightEmpty: Boolean
        get() = defaultSharedPreferences
                .getString(getString(R.string.pref_height_key),
                        getString(R.string.pref_height_value_default))!!
                .isEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DWLApplication.app().appComponent().inject(this)
        setTheme(R.style.AppThemeBar)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        viewModel!!.weightEntered.observe(this, Observer { weightEntered ->
            if (weightEntered!!) {
                displayWeightAddedUI()
            } else {
                displayWeightInputUI()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, SETTINGS_REQUEST_CODE)
        }

        return true
    }

    /**
     * Triggered when add button is pressed
     *
     * @param view reference to the button, not used here.
     */
    fun addWeight(view: View) {

        val weight: Float
        val fatPc: Float?

        val etWeight: EditText?
        val etFatPc: EditText?

        if (doesUseFatPc()) {
            etWeight = binding.inputLayout?.etTodayWeight
            etFatPc = binding.inputLayout?.etFatPc
        } else {
            etWeight = binding.inputNoFat?.etTodayWeight
            etFatPc = null
        }
        // exit if no weight
        if (TextUtils.isEmpty(etWeight?.text)) {
            return
        }

        // set error and exit if weight is wrong
        try {
            weight = java.lang.Float.parseFloat(etWeight?.text.toString())
        } catch (nfe: NumberFormatException) {
            nfe.printStackTrace()
            etWeight?.error = "Enter correct weight"
            return
        }

        fatPc = etFatPc?.text.toString().toFloatOrNull() ?: 0f

        if(fatPc==0f) etFatPc?.error = "Enter correct fat percentage"


        /*// set error and exit if fat % is wrong
        if (etFatPc != null) {
            try {
                fatPc = java.lang.Float.parseFloat(etFatPc.text.toString())
            } catch (nfe: NumberFormatException) {
                nfe.printStackTrace()
                etFatPc.error = "Enter correct fat percentage"
                return
            }

        }*/

        viewModel!!.addData(weight, fatPc)
    }

    private fun displayWeightAddedUI() {
        binding.inputLayout?.root?.visibility = View.INVISIBLE
        binding.inputNoFat?.root?.visibility = View.INVISIBLE
        binding.todayLayout?.root?.visibility = View.VISIBLE

        // we put this here because only when stuff is added we need to schedule another one
       // ReminderUtils.scheduleWeightReminder(this)
    }

    private fun displayWeightInputUI() {
        binding.todayLayout?.root?.visibility = View.INVISIBLE

        if (doesUseFatPc()) {
            binding.inputNoFat?.root?.visibility = View.INVISIBLE
            binding.inputLayout?.root?.visibility = View.VISIBLE
            binding.inputLayout?.etTodayWeight?.setText("")
            binding.inputLayout?.etFatPc?.setText("")
        } else {
            binding.inputLayout?.root?.visibility = View.INVISIBLE
            binding.inputNoFat?.root?.visibility = View.VISIBLE
        }

        //TODO unschedule any reminders


    }

    private fun doesUseFatPc(): Boolean {
        return defaultSharedPreferences
                .getBoolean(getString(R.string.pref_uses_fat_pc_key),
                        resources.getBoolean(R.bool.pref_uses_fat_pc_default))
    }

    private fun doesUseBMI(): Boolean {
        return defaultSharedPreferences
                .getBoolean(getString(R.string.pref_uses_bmi_key),
                        resources.getBoolean(R.bool.pref_uses_bmi_default))
    }

    private fun setUpTodayWeights(data: Cursor) {
        if (todayWeightAdapter == null) {
           // todayWeightAdapter = WeightAdapter(data, 1, defaultSharedPreferences)
            binding.todayLayout?.rvToday?.adapter = todayWeightAdapter
            binding.todayLayout?.rvToday?.layoutManager =
                LinearLayoutManager(this)
            binding.todayLayout?.rvToday?.setHasFixedSize(true)
        } else {
            binding.todayLayout?.rvToday?.adapter = todayWeightAdapter
            todayWeightAdapter!!.updateData(data)
            todayWeightAdapter!!.notifyDataSetChanged()
        }
    }

    private fun setUpPreviousWeights(data: Cursor) {

        if (weightEnteredToday) {
           // previousWeightAdapter = WeightAdapter(data, PREVIOUS_NO_TODAY, defaultSharedPreferences)
        } else {
         //   previousWeightAdapter = WeightAdapter(data, PREVIOUS, defaultSharedPreferences)
        }

        binding.layoutPrevious?.rvPrevious?.adapter = previousWeightAdapter
        binding.layoutPrevious?.rvPrevious?.layoutManager =
            LinearLayoutManager(this)
        binding.layoutPrevious?.rvPrevious?.setHasFixedSize(true)
    }

 /*   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_REQUEST_CODE) {
            val changed = data.getBooleanExtra(PREFERENCES_CHANGED, false)
            if (changed) {
                // this is a fallout from using PreferenceFragment in SettingsActivityas we can't
                // custimise it to cater for this scenario
                if (doesUseBMI() && isHeightEmpty) {
                    setNoBMI()
                }
                if (todayWeightAdapter != null) {
                    Log.d(TAG, "todayWeightAdapter is not null")
                    todayWeightAdapter!!.notifyColumnsChanged()
                } else {
                    Log.d(TAG, "todayWeightAdapter is null")
                }
                previousWeightAdapter!!.notifyColumnsChanged()
                if (binding.inputLayout?.root?.visibility == View.VISIBLE || binding.inputNoFat?.root?.visibility == View.VISIBLE) {
                    displayWeightInputUI()
                }
            }
        }
    }*/

    /**
     * We manually switch the BMI off as no height.
     */

    @SuppressLint("ApplySharedPref")
    private fun setNoBMI() {
        defaultSharedPreferences
                .edit()
                .putBoolean(getString(R.string.pref_uses_bmi_key), false)
                .commit()
    }

    companion object {

        private const val TAG = "Main Activity"
        const val SETTINGS_REQUEST_CODE = 15
        const val PREFERENCES_CHANGED = "changed_prefs"
    }
}