package com.leadinsource.dailyweightlog.ui.main

import com.leadinsource.dailyweightlog.R
import com.leadinsource.dailyweightlog.db.Weight
import java.util.*

class DefaultItemViewModel(val date: Date, val weight: String, val fatPc: String, val bmi: String="") : ItemViewModel {
    override val layoutId: Int
        get() = R.layout.history_list_item

}