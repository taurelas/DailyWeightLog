package com.leadinsource.dailyweightlog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadinsource.dailyweightlog.db.ExampleData;

/**
 * Responsible for displaying the history of weightings
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 1000;
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
