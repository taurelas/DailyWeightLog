package com.leadinsource.dailyweightlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leadinsource.dailyweightlog.db.DataContract;
import com.leadinsource.dailyweightlog.utils.Units;

import java.sql.Timestamp;

/**
 * Responsible for displaying the history of weightings
 */

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int TODAY = 1;
    static final int PREVIOUS = 3;
    static final int PREVIOUS_NO_TODAY = 2;
    private static final String TAG = "WeightAdapter";

    private Cursor cursor;
    private int displayElements = 0;
    private int offsetPosition = 0;
    private Context context;
    private boolean usesFatPc;
    private boolean usesBMI;

    WeightAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    WeightAdapter(Cursor cursor, int displayElements) {
        this.cursor = cursor;
        this.displayElements = displayElements;
        if (displayElements == PREVIOUS_NO_TODAY) {
            offsetPosition = 1;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
        usesFatPc = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_uses_fat_pc_key),
                        context.getResources().getBoolean(R.bool.pref_uses_fat_pc_default));
        usesBMI = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_uses_bmi_key),
                        context.getResources().getBoolean(R.bool.pref_uses_bmi_default));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d(TAG, "Binding position "+position);

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


        if(!usesFatPc) {
            holder.tvFatPc.setVisibility(View.GONE);
        } else {
            float fatPc = cursor.getFloat(cursor.getColumnIndex(DataContract.WeightEntry.COLUMN_FAT_PC));
            holder.tvFatPc.setText(Units.getFatPcString(fatPc));
            holder.tvFatPc.setVisibility(View.VISIBLE);
        }

        // we can't use height in float due to SettingsFragment
        // long story short we tried and it didn't work.
        float height = 0f;
        String heightString = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_height_key), "");

        try {
            height = Float.parseFloat(heightString);
        } catch (NumberFormatException nfe) {
            Log.e("WeightAdapter", "Unable to parse height from settings");
            nfe.printStackTrace();
        }
        if(!usesBMI) {
            holder.tvBMI.setVisibility(View.GONE);
        } else {
            holder.tvBMI.setVisibility(View.VISIBLE);
            holder.tvBMI.setText(Units.getMetricBMIString(height, weight));
        }

    }

    void notifyColumnsChanged() {
        usesFatPc = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_uses_fat_pc_key),
                        context.getResources().getBoolean(R.bool.pref_uses_fat_pc_default));
        usesBMI = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(context.getString(R.string.pref_uses_bmi_key),
                        context.getResources().getBoolean(R.bool.pref_uses_bmi_default));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (displayElements > 0) {
            if (displayElements == PREVIOUS_NO_TODAY) {
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(context, "Changed!", Toast.LENGTH_SHORT).show();
        if (key.equals(context.getString(R.string.pref_uses_fat_pc_key))) {
            usesFatPc = sharedPreferences.getBoolean(key,
                    context.getResources().getBoolean(R.bool.pref_uses_fat_pc_default));
        }
        if(key.equals(context.getString(R.string.pref_uses_bmi_key))) {
            usesBMI = sharedPreferences.getBoolean(key,
                    context.getResources().getBoolean(R.bool.pref_uses_bmi_default));
        }

        notifyDataSetChanged();
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
