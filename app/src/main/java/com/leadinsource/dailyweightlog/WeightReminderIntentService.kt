package com.leadinsource.dailyweightlog

import android.app.IntentService
import android.content.Intent

import com.leadinsource.dailyweightlog.utils.Notifications

/**
 * Created by Matt on 05/02/2018.
 */
private const val INTENT_SERVICE_NAME = "WeightReminderIntentService"

class WeightReminderIntentService : IntentService(INTENT_SERVICE_NAME) {

    override fun onHandleIntent(intent: Intent?) {
        Notifications.clearAllNotifications(this)
    }
}
