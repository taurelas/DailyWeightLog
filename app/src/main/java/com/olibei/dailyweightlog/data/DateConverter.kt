package com.olibei.dailyweightlog.data

import android.content.Context
import android.text.format.DateFormat
import java.util.*
import javax.inject.Inject

class DateConverter @Inject constructor(context: Context) {

    private val dateFormat = DateFormat.getDateFormat(context)

    fun getString(date: Date) = dateFormat.format(date)

}