package com.leadinsource.dailyweightlog.utils

/*import android.content.Context
import androidx.core.util.TimeUtils

import com.firebase.jobdispatcher.Constraint
import com.firebase.jobdispatcher.Driver
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Job
import com.firebase.jobdispatcher.Lifetime
import com.firebase.jobdispatcher.Trigger

import java.util.Date
import java.util.concurrent.TimeUnit

*//**
 * Created by Matt on 05/02/2018.
 *//*
private const val REMINDER_JOB_TAG = "weight_reminder_tag"

object ReminderUtils {

    private var initialized: Boolean = false

    @Synchronized
    fun scheduleWeightReminder(context: Context) {
        if (initialized) return

        val driver = GooglePlayDriver(context)
        val dispatcher = FirebaseJobDispatcher(driver)

        val currentHour = Date(System.currentTimeMillis()).hours

        val hoursAhead =
                if (currentHour < 7) {
                    7 - currentHour
                } else {
                    24 - currentHour + 7
                }

        val hoursAheadInSeconds = TimeUnit.HOURS.toSeconds(hoursAhead.toLong()).toInt()

        val constraintReminderJob = dispatcher.newJobBuilder()
                .setService(ReminderJobService::class.java)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(hoursAheadInSeconds, hoursAheadInSeconds + 1800))
                .setReplaceCurrent(true)
                .build()

        dispatcher.schedule(constraintReminderJob)
        initialized = true
    }
}*/
