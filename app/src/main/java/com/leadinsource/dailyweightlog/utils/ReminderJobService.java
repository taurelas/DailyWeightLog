package com.leadinsource.dailyweightlog.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Matt on 05/02/2018.
 */

public class ReminderJobService extends JobService {
    private AsyncTask backgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = ReminderJobService.this;
                Notifications.remindUserToWeigh(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };

        backgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(backgroundTask!= null) {
            backgroundTask.cancel(true);
        }
        return true;
    }
}
