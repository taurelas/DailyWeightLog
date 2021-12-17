package com.leadinsource.dailyweightlog.ui.main

import android.text.format.DateFormat
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leadinsource.dailyweightlog.BindableRecyclerViewAdapter
import java.util.*

@BindingAdapter("itemViewModels")
fun bindItemViewModels(recyclerView: RecyclerView, itemViewModels: List<ItemViewModel>) {
    val adapter = getOrCreateAdapter(recyclerView)
    adapter.updateItems(itemViewModels)
}

fun getOrCreateAdapter(recyclerView: RecyclerView): BindableRecyclerViewAdapter{
    return if(recyclerView.adapter != null && recyclerView.adapter is BindableRecyclerViewAdapter) {
        recyclerView.adapter as BindableRecyclerViewAdapter
    } else {
        val bindableRecyclerViewAdapter = BindableRecyclerViewAdapter()
        recyclerView.adapter = bindableRecyclerViewAdapter
        bindableRecyclerViewAdapter
    }
}

@BindingAdapter("localDate")
fun bindLocalDate(textView: TextView, date: Date) {
    val dateFormat = DateFormat.getDateFormat(textView.context)
    textView.text = dateFormat.format(date)
}
