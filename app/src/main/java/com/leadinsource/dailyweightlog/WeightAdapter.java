package com.leadinsource.dailyweightlog;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadinsource.dailyweightlog.db.DataContract;
import com.leadinsource.dailyweightlog.utils.Units;

import java.sql.Timestamp;

/**
 * Responsible for displaying the history of weightings
 */

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {

    public static final int TODAY = 1;
    public static final int PREVIOUS = 3;
    public static final int PREVIOUS_NO_TODAY = 2;

    private Cursor cursor;
    private int displayElements = 0;
    private int offsetPosition=0;
    private Context context;

    WeightAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    WeightAdapter(Cursor cursor, int displayElements) {
        this.cursor = cursor;
        this.displayElements = displayElements;
        if(displayElements==PREVIOUS_NO_TODAY) {
            offsetPosition = 1;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int dataPosition = position + offsetPosition;

        // moving the cursor to the position, but return if it can't go there
        if (!cursor.moveToPosition(dataPosition)) {
            return;
        }

        String date = cursor.getString(cursor.getColumnIndex(DataContract.WeightEntry.COLUMN_DATE));
        Timestamp timestamp = Timestamp.valueOf(date);

        holder.tvDate.setText(DateFormat.getDateFormat(holder.tvDate.getContext()).format(timestamp));

        float weight = cursor.getFloat(cursor.getColumnIndex(DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG));

        holder.tvWeight.setText(Units.getWeightTextWithUnits(weight));

        float fatPc = cursor.getFloat(cursor.getColumnIndex(DataContract.WeightEntry.COLUMN_FAT_PC));

        holder.tvFatPc.setText(Units.getFatPcString(fatPc));


        float height = 0f;
        String heightString = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_height_key), "");

        try {
            height = Float.parseFloat(heightString);
        } catch (NumberFormatException nfe) {
            Log.e("WeightAdapter", "Unable to parse height from settings");
            nfe.printStackTrace();
        }

        holder.tvBMI.setText(Units.getMetricBMIString(height, weight));

    }

    @Override
    public int getItemCount() {

        if (displayElements > 0) {
            if(displayElements==PREVIOUS_NO_TODAY) {
                return PREVIOUS;
            } else {
                return displayElements;
            }
        }

        return cursor.getCount();
    }

    void updateData(Cursor cursor) {
        this.cursor = cursor;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvWeight, tvFatPc, tvBMI;

        ViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvFatPc = itemView.findViewById(R.id.tvFatPc);
            tvBMI = itemView.findViewById(R.id.tvBMI);
        }

    }
}
