package com.olibei.dailyweightlog

import android.content.SharedPreferences
import android.database.Cursor
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View

import com.olibei.dailyweightlog.app.DWLApplication
import com.olibei.dailyweightlog.databinding.ActivityHistoryBinding

import javax.inject.Inject

class HistoryActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var adapter: WeightAdapter? = null
    lateinit var binding: ActivityHistoryBinding

    @Inject
    lateinit var defaultSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DWLApplication.app().appComponent().inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)

        if (!doesUseFatPc()) {
            binding.tvFatPcHeader.visibility = View.GONE
        }
        if (!doesUseBMI()) {
            binding.tvBMIHeader.visibility = View.GONE
        }
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

    internal fun setUpRecyclerView(cursor: Cursor) {
        val usesFatPc = defaultSharedPreferences!!
            .getBoolean(getString(R.string.pref_uses_fat_pc_key),
                resources.getBoolean(R.bool.pref_uses_fat_pc_default))
        val usesBMI = defaultSharedPreferences!!
            .getBoolean(getString(R.string.pref_uses_bmi_key),
                resources.getBoolean(R.bool.pref_uses_bmi_default))


        adapter = WeightAdapter(
            emptyArray<Data>(), usesBMI, usesFatPc)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        adapter!!.notifyDataSetChanged()
    }

    companion object {

        private val WEIGHT_LOADER_ID = 0
    }
}
