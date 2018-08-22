package com.leadinsource.dailyweightlog.utils

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import kotlinx.coroutines.experimental.launch

/**
 * Created by Matt on 05/02/2018.
 */

class ReminderJobService : JobService() {

/** called by the system when it is time for your job to execute*/
    override fun onStartJob(job: JobParameters): Boolean {
        launch {
            backgroundTask()

        }

        jobFinished(job, false)
        return true

    }

    private fun backgroundTask() {
        val context = this@ReminderJobService
        Notifications.remindUserToWeigh(context)
    }

    // run when job is cancelled
    override fun onStopJob(job: JobParameters): Boolean {

        return true
    }
}
