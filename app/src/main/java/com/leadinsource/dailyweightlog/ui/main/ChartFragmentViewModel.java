package com.leadinsource.dailyweightlog.ui.main;

import android.arch.lifecycle.ViewModel;
import android.database.Cursor;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.leadinsource.dailyweightlog.R;
import com.leadinsource.dailyweightlog.db.DataContract;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Matt on 27/02/2018.
 */

public class ChartFragmentViewModel extends ViewModel {


    LineData getChartData(Cursor data) {
        ArrayList<Entry> entries = new ArrayList<>();

        data.moveToFirst();
        while (!data.isAfterLast()) {
            long id = data.getLong(data.getColumnIndex(DataContract.WeightEntry._ID));
            float weight = data.getFloat(data.getColumnIndex(DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG));
            //Log.d("Chart", "adding " + id + " / " + weight);
            entries.add(new Entry(id, weight));
            data.moveToNext();
        }

        Collections.reverse(entries);
        LineDataSet dataSet = new LineDataSet(entries, "Weight in time");
        //dataSet.setColor(getResources().getColor(R.color.colorAccent));
        return new LineData(dataSet);
    }
}
