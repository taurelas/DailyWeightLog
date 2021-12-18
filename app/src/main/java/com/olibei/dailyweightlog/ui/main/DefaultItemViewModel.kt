package com.olibei.dailyweightlog.ui.main

import com.olibei.dailyweightlog.R
import java.util.*

class DefaultItemViewModel(val date: Date, val weight: String, val fatPc: String, val bmi: String="") : ItemViewModel {
    override val layoutId: Int
        get() = R.layout.history_list_item

}