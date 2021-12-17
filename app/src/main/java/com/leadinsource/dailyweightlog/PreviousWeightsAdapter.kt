package com.leadinsource.dailyweightlog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leadinsource.dailyweightlog.databinding.HistoryListItemBinding
import com.leadinsource.dailyweightlog.databinding.HistoryListItemBinding.inflate
import com.leadinsource.dailyweightlog.db.Weight
import com.leadinsource.dailyweightlog.utils.Units

/**
 * Responsible for displaying the history of weightings
 */

internal class PreviousWeightsAdapter(private val dataSet: List<Weight>) : RecyclerView.Adapter<PreviousWeightsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: HistoryListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.tvDate.text = dataSet[position].date?.date?.toString() ?: ""

        val weight = dataSet[position].weightInKg

        holder.binding.tvWeight.text = Units.getWeightTextWithUnits(weight)

        holder.binding.tvFatPc.text = Units.getFatPcString(dataSet[position].fatPc)


            val fatPc = dataSet[position].fatPc
            holder.binding.tvFatPc.text = Units.getFatPcString(fatPc)


        // we can't use height in float due to SettingsFragment
        // long story short we tried and it didn't work.
        //var height = 0f
        //val heightString = defaultSharedPreferences!!.getString(context!!.getString(R.string.pref_height_key), "")

        /*try {
            height = java.lang.Float.parseFloat(heightString)
        } catch (nfe: NumberFormatException) {
            nfe.printStackTrace()
        }*/

        /*if (!usesBMI) {
            holder.binding.tvBMI.visibility = View.GONE
        } else {
            holder.binding.tvBMI.visibility = View.VISIBLE
            holder.binding.tvBMI.text = Units.getMetricBMIString(height, weight)
        }*/

    }

    override fun getItemCount() = dataSet.size

}
