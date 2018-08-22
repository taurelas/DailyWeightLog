package com.leadinsource.dailyweightlog.ui.main

import android.arch.lifecycle.ViewModel
import android.database.Cursor
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.leadinsource.dailyweightlog.db.COLUMN_WEIGHT_IN_KG
import com.leadinsource.dailyweightlog.db._ID
import java.util.*

/**
 * Created by Matt on 27/02/2018.
 */

class ChartFragmentViewModel : ViewModel() {


    internal fun getChartData(data: Cursor): LineData {
        val entries = ArrayList<Entry>()

        data.moveToFirst()
        while (!data.isAfterLast) {
            val id = data.getLong(data.getColumnIndex(_ID))
            val weight = data.getFloat(data.getColumnIndex(COLUMN_WEIGHT_IN_KG))
            //Log.d("Chart", "adding " + id + " / " + weight);
            entries.add(Entry(id.toFloat(), weight))
            data.moveToNext()
        }

        entries.reverse()
        val dataSet = LineDataSet(entries, "Weight in time")
        //dataSet.setColor(getResources().getColor(R.color.colorAccent));
        return LineData(dataSet)
    }
}
