package com.leadinsource.dailyweightlog

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.leadinsource.dailyweightlog.db.COLUMN_DATE
import com.leadinsource.dailyweightlog.db.COLUMN_FAT_PC
import com.leadinsource.dailyweightlog.db.COLUMN_WEIGHT_IN_KG
import com.leadinsource.dailyweightlog.utils.Units
import java.sql.Timestamp

/**
 * Responsible for displaying the history of weightings
 */
const val TODAY = 1
const val PREVIOUS = 3
const val PREVIOUS_NO_TODAY = 2
private const val TAG = "WeightAdapter"

internal class WeightAdapter : RecyclerView.Adapter<WeightAdapter.ViewHolder>, SharedPreferences.OnSharedPreferenceChangeListener {

    private var cursor: Cursor? = null
    private var displayElements = 0
    private var offsetPosition = 0
    private var context: Context? = null
    private var usesFatPc: Boolean = false
    private var usesBMI: Boolean = false
    private var defaultSharedPreferences: SharedPreferences? = null

    constructor(cursor: Cursor, defaultSharedPreferences: SharedPreferences) {
        this.cursor = cursor
        this.defaultSharedPreferences = defaultSharedPreferences
    }

    constructor(cursor: Cursor, displayElements: Int, defaultSharedPreferences: SharedPreferences) {
        this.cursor = cursor
        this.displayElements = displayElements
        if (displayElements == PREVIOUS_NO_TODAY) {
            offsetPosition = 1
        }
        this.defaultSharedPreferences = defaultSharedPreferences
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
        usesFatPc = defaultSharedPreferences!!
                .getBoolean(context!!.getString(R.string.pref_uses_fat_pc_key),
                        context!!.resources.getBoolean(R.bool.pref_uses_fat_pc_default))
        usesBMI = defaultSharedPreferences!!
                .getBoolean(context!!.getString(R.string.pref_uses_bmi_key),
                        context!!.resources.getBoolean(R.bool.pref_uses_bmi_default))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.history_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.d(TAG, "Binding position $position")

        val dataPosition = position + offsetPosition

        // moving the cursor to the position, but return if it can't go there
        if (!cursor!!.moveToPosition(dataPosition)) {
            return
        }

        val date = cursor!!.getString(cursor!!.getColumnIndex(COLUMN_DATE))
        val timestamp = Timestamp.valueOf(date)

        holder.tvDate.text = DateFormat.getDateFormat(holder.tvDate.context).format(timestamp)

        val weight = cursor!!.getFloat(cursor!!.getColumnIndex(COLUMN_WEIGHT_IN_KG))

        holder.tvWeight.text = Units.getWeightTextWithUnits(weight)

        if (!usesFatPc) {
            holder.tvFatPc.visibility = View.GONE
        } else {
            val fatPc = cursor!!.getFloat(cursor!!.getColumnIndex(COLUMN_FAT_PC))
            holder.tvFatPc.text = Units.getFatPcString(fatPc)
            holder.tvFatPc.visibility = View.VISIBLE
        }

        // we can't use height in float due to SettingsFragment
        // long story short we tried and it didn't work.
        var height = 0f
        val heightString = defaultSharedPreferences!!.getString(context!!.getString(R.string.pref_height_key), "")

        try {
            height = java.lang.Float.parseFloat(heightString)
        } catch (nfe: NumberFormatException) {
            nfe.printStackTrace()
        }

        if (!usesBMI) {
            holder.tvBMI.visibility = View.GONE
        } else {
            holder.tvBMI.visibility = View.VISIBLE
            holder.tvBMI.text = Units.getMetricBMIString(height, weight)
        }

    }

    fun notifyColumnsChanged() {
        usesFatPc = defaultSharedPreferences!!
                .getBoolean(context!!.getString(R.string.pref_uses_fat_pc_key),
                        context!!.resources.getBoolean(R.bool.pref_uses_fat_pc_default))
        usesBMI = defaultSharedPreferences!!
                .getBoolean(context!!.getString(R.string.pref_uses_bmi_key),
                        context!!.resources.getBoolean(R.bool.pref_uses_bmi_default))
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {

        return if (displayElements > 0) {
            if (displayElements == PREVIOUS_NO_TODAY) {
                PREVIOUS
            } else {
                displayElements
            }
        } else cursor!!.count

    }

    fun updateData(cursor: Cursor) {
        this.cursor = cursor
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        Toast.makeText(context, "Changed!", Toast.LENGTH_SHORT).show()
        if (key == context!!.getString(R.string.pref_uses_fat_pc_key)) {
            usesFatPc = sharedPreferences.getBoolean(key,
                    context!!.resources.getBoolean(R.bool.pref_uses_fat_pc_default))
        }
        if (key == context!!.getString(R.string.pref_uses_bmi_key)) {
            usesBMI = sharedPreferences.getBoolean(key,
                    context!!.resources.getBoolean(R.bool.pref_uses_bmi_default))
        }

        notifyDataSetChanged()
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.tvDate)
        lateinit var tvDate: TextView
        @BindView(R.id.tvWeight)
        lateinit var tvWeight: TextView
        @BindView(R.id.tvFatPc)
        lateinit var tvFatPc: TextView
        @BindView(R.id.tvBMI)
        lateinit var tvBMI: TextView

        init {
            ButterKnife.bind(this, itemView)

        }

    }
}
