package com.leadinsource.dailyweightlog.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.TimeUtils;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matt on 05/02/2018.
 */

public class ReminderUtils {
    private static final String REMINDER_JOB_TAG = "weight_reminder_tag";
    private static boolean initialized;

    synchronized public static void scheduleWeightReminder(@NonNull final Context context) {
        if(initialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        int hoursAhead = 0;

        int currentHour = new Date(System.currentTimeMillis()).getHours();

        if(currentHour<7) {
            hoursAhead = 7-currentHour;
        } else {
            hoursAhead = 24-currentHour + 7;
        }

        int hoursAheadInSeconds = (int) TimeUnit.HOURS.toSeconds(hoursAhead);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(ReminderJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(hoursAheadInSeconds, hoursAheadInSeconds+ 1800))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);
        initialized = true;
    }
}
