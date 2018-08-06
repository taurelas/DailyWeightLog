package com.leadinsource.dailyweightlog.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.leadinsource.dailyweightlog.ui.main.MainActivity;
import com.leadinsource.dailyweightlog.R;
import com.leadinsource.dailyweightlog.WeightReminderIntentService;

/**
 * Created by Matt on 05/02/2018.
 */

public class Notifications {

    private static final int WEIGH_REMINDER_PENDING_INTENT_ID = 3426;
    private static final int WEIGH_REMINDER_NOTIFICATION_ID = 1234;
    private static final String WEIGH_REMINDER_NOTIFICATION_CHANNEL_ID = "weigh_reminder";

    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    private static PendingIntent contentIntent(Context context) {

        Intent startActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                WEIGH_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, android.R.drawable.ic_lock_idle_alarm);

        return largeIcon;
    }

    public static void remindUserToWeigh(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WEIGH_REMINDER_NOTIFICATION_CHANNEL_ID,
                    "Weigh Reminder",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, WEIGH_REMINDER_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setLargeIcon(largeIcon(context))
                        .setContentTitle("Weigh yourself")
                        .setContentText("Don't drink or eat anything before and wear as little clothing as possible")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Don't drink or eat anything before and wear as little clothing as possible"))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .addAction(ignoreReminderAction(context))
                        .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(WEIGH_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }

    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, WeightReminderIntentService.class);

        ignoreReminderIntent.setAction(ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(
                android.R.drawable.ic_notification_clear_all,
                "I can't",
                ignoreReminderPendingIntent);

        return ignoreReminderAction;

    }



}
