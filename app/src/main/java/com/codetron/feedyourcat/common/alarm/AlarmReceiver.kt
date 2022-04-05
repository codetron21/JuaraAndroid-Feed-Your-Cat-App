package com.codetron.feedyourcat.common.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.codetron.feedyourcat.common.notification.AppNotification
import java.util.*

private const val TAG = "AlarmReceiver"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")

        if (context == null || intent == null) return

        val channelId = intent.getStringExtra(KEY_CHANNEL_ID) ?: return
        val notificationId = intent.getIntExtra(KEY_NOTIFICATION_ID, 0)
        val channelName = intent.getStringExtra(KEY_CHANNEL_NAME) ?: return
        val title = intent.getStringExtra(KEY_TITLE) ?: return
        val message = intent.getStringExtra(KEY_MESSAGE) ?: return
        val destination = intent.getStringExtra(KEY_DESTINATION) ?: return

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, Class.forName(destination)),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val appNotification = AppNotification(
            context,
            notificationId,
            channelId,
            channelName,
            title,
            message,
            pendingIntent
        )

        appNotification.launch()
    }

    fun setAlarm(
        context: Context,
        notificationId: Int,
        channelId: String,
        channelName: String,
        title: String,
        message: String,
        times: Int,
        destination: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
            .apply {
                putExtra(KEY_TITLE, title)
                putExtra(KEY_MESSAGE, message)
                putExtra(KEY_CHANNEL_ID, channelId)
                putExtra(KEY_CHANNEL_NAME, channelName)
                putExtra(KEY_NOTIFICATION_ID, notificationId)
                putExtra(KEY_DESTINATION, destination)
            }

        val hour = times / 60
        val minutes = times % 60

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minutes)
        calendar.set(Calendar.SECOND, 0)

        val time = calendar.timeInMillis

        val pendingIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                } else 0
            )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, id, intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            } else 0
        )
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    fun isAlarmSet(context: Context, id: Int): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            id,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            } else 0
        ) != null
    }

    companion object {
        const val KEY_NOTIFICATION_ID = "KEY_NOTIFICATION_ID"
        const val KEY_CHANNEL_ID = "KEY_CHANNEL_ID"
        const val KEY_CHANNEL_NAME = "KEY_CHANNEL_NAME"
        const val KEY_TITLE = "KEY_TITLE"
        const val KEY_MESSAGE = "KEY_MESSAGE"
        const val KEY_DESTINATION = "KEY_DESTINATION"
    }
}