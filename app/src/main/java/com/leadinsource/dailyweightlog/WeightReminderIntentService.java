package com.leadinsource.dailyweightlog;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.leadinsource.dailyweightlog.utils.Notifications;

/**
 * Created by Matt on 05/02/2018.
 */

public class WeightReminderIntentService extends IntentService {

    public WeightReminderIntentService() {
        super("WeightReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Notifications.clearAllNotifications(this);
    }
}
