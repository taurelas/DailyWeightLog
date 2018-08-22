package com.leadinsource.dailyweightlog.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat

import com.leadinsource.dailyweightlog.ui.main.MainActivity
import com.leadinsource.dailyweightlog.R
import com.leadinsource.dailyweightlog.WeightReminderIntentService

/**
 * Created by Matt on 05/02/2018.
 */

object Notifications {

    private const val WEIGH_REMINDER_PENDING_INTENT_ID = 3426
    private const val WEIGH_REMINDER_NOTIFICATION_ID = 1234
    private const val WEIGH_REMINDER_NOTIFICATION_CHANNEL_ID = "weigh_reminder"

    private const val ACTION_DISMISS_NOTIFICATION = "dismiss-notification"
    private const val ACTION_IGNORE_PENDING_INTENT_ID = 14

    private fun contentIntent(context: Context): PendingIntent {

        val startActivityIntent = Intent(context, MainActivity::class.java)

        return PendingIntent.getActivity(
                context,
                WEIGH_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun largeIcon(context: Context): Bitmap {
        val res = context.resources

        return BitmapFactory.decodeResource(res, android.R.drawable.ic_lock_idle_alarm)
    }

    fun remindUserToWeigh(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    WEIGH_REMINDER_NOTIFICATION_CHANNEL_ID,
                    "Weigh Reminder",
                    NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, WEIGH_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Weigh yourself")
                .setContentText("Don't drink or eat anything before and wear as little clothing as possible")
                .setStyle(NotificationCompat.BigTextStyle().bigText("Don't drink or eat anything before and wear as little clothing as possible"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }

        notificationManager.notify(WEIGH_REMINDER_NOTIFICATION_ID, notificationBuilder.build())
    }

    fun clearAllNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancelAll()
    }

    private fun ignoreReminderAction(context: Context): NotificationCompat.Action {
        val ignoreReminderIntent = Intent(context, WeightReminderIntentService::class.java)

        ignoreReminderIntent.action = ACTION_DISMISS_NOTIFICATION

        val ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)


        return NotificationCompat.Action(
                android.R.drawable.ic_notification_clear_all,
                "I can't",
                ignoreReminderPendingIntent)

    }


}
