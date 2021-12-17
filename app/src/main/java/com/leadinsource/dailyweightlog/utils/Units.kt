package com.leadinsource.dailyweightlog.utils

import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

/**
 * Provides utility methods related to unit conversion and display
 */

object Units {
    fun getHeightTextWithUnits(height: String): String {
        return if (height.isEmpty()) {
            ""
        } else {
            "$height cm"
        }
    }

    private fun getMetricBMI(height: Float, weight: Float): Float {
        return round(weight / (height / 100 * (height / 100)), 1)
    }

    fun getMetricBMIString(height: Float, weight: Float): String {
        return if (height > 0) {
            getMetricBMI(height, weight).toString() + ""
        } else {
            "-"
        }
    }

    private fun round(value: Float, precision: Int): Float {
        var bigDecimal = BigDecimal(value.toDouble())
        bigDecimal = bigDecimal.setScale(precision, BigDecimal.ROUND_UP)

        return bigDecimal.toFloat()
    }

    fun getWeightTextWithUnits(weight: Float): String {

        return weight.toString() + " kg"
    }

    fun stripTime(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.time
    }

    fun getFatPcString(fatPc: Float?) = when (fatPc) {
        null, 0.0f -> ""
        else -> "$fatPc %"
    }
}
