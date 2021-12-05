package com.leadinsource.dailyweightlog

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.leadinsource.dailyweightlog.databinding.HistoryListItemBinding
import com.leadinsource.dailyweightlog.databinding.HistoryListItemBinding.inflate
import com.leadinsource.dailyweightlog.db.COLUMN_DATE
import com.leadinsource.dailyweightlog.utils.Units
import java.sql.Timestamp

/**
 * Responsible for displaying the history of weightings
 */
const val TODAY = 1
const val PREVIOUS = 3
const val PREVIOUS_NO_TODAY = 2
private const val TAG = "WeightAdapter"

data class Data(val date: String, val weight: Float, val fatPc: Float)

internal class WeightAdapter(private val dataSet: Array<Data>, private val usesBMI: Boolean, private val usesFatPc: Boolean, private val displayElements: Int = 0) : RecyclerView.Adapter<WeightAdapter.ViewHolder>() {

    private var cursor: Cursor? = null
    private var offsetPosition = 0
    private var context: Context? = null
    private var defaultSharedPreferences: SharedPreferences? = null

    init {
        if (displayElements == PREVIOUS_NO_TODAY) {
            offsetPosition = 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: HistoryListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPosition = position + offsetPosition

        val date = cursor!!.getString(cursor!!.getColumnIndex(COLUMN_DATE))
        val timestamp = Timestamp.valueOf(date)

        holder.binding.tvDate.text = dataSet[position].date

        val weight = dataSet[position].weight

        holder.binding.tvWeight.text = Units.getWeightTextWithUnits(weight)

        if (!usesFatPc) {
            holder.binding.tvFatPc.visibility = View.GONE
        } else {
            val fatPc = dataSet[position].fatPc
            holder.binding.tvFatPc.text = Units.getFatPcString(fatPc)
            holder.binding.tvFatPc.visibility = View.VISIBLE
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
            holder.binding.tvBMI.visibility = View.GONE
        } else {
            holder.binding.tvBMI.visibility = View.VISIBLE
            holder.binding.tvBMI.text = Units.getMetricBMIString(height, weight)
        }

    }

    fun notifyColumnsChanged() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {

        return if (displayElements > 0) {
            if (displayElements == PREVIOUS_NO_TODAY) {
                PREVIOUS
            } else {
                displayElements
            }
        } else dataSet.size

    }

    fun updateData(cursor: Cursor) {
        this.cursor = cursor
    }

}
