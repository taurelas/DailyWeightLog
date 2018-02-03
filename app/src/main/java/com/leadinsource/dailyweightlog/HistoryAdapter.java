package com.leadinsource.dailyweightlog;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadinsource.dailyweightlog.db.DataContract;
import com.leadinsource.dailyweightlog.db.ExampleData;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Responsible for displaying the history of weightings
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Cursor cursor;

    public HistoryAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // moving the cursor to the position, but return if it can't go there
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String date = cursor.getString(cursor.getColumnIndex(DataContract.WeightEntry.COLUMN_DATE));
        Timestamp timestamp = Timestamp.valueOf(date);

        timestamp.getDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.tvDate.setText(dateFormat.format(timestamp));

        /*try {
            holder.tvDate.setText(dateFormat.parse(date).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        float weight = cursor.getFloat(cursor.getColumnIndex(DataContract.WeightEntry.COLUMN_WEIGHT_IN_KG));

        holder.tvWeight.setText(Units.getWeightTextWithUnits(weight));

        float fatPc = cursor.getFloat(cursor.getColumnIndex(DataContract.WeightEntry.COLUMN_FAT_PC));

        holder.tvFatPc.setText(fatPc + " %");

        holder.tvBMI.setText(Units.getMetricBMI(173f, weight)+"");

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
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

        void bind(int listIndex) {
            tvDate.setText(ExampleData.getDate(listIndex));
            tvWeight.setText(ExampleData.getWeight(listIndex));
            tvFatPc.setText(ExampleData.getFatPc(listIndex));
            tvBMI.setText(String.valueOf(Units.getMetricBMI(173f, Float.parseFloat(ExampleData.getWeight(listIndex)))));
        }
    }


}
