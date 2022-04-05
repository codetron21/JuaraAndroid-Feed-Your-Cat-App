package com.codetron.feedyourcat.common.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.codetron.feedyourcat.R

class AppNotification(
    private val context: Context,
    private val notificationId: Int,
    private val channelId: String,
    private val channelName: String,
    private val title: String,
    private val message: String,
    private val pendingIntent: PendingIntent? = null
) {

    fun launch() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = createNotification(title, message, pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createNotificationChannel()
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notification)
    }

    private fun createNotification(
        title: String,
        message: String,
        pendingIntent: PendingIntent? = null
    ): Notification {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return NotificationCompat.Builder(context, channelId)
            .setChannelId(channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_notification
                )
            )
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = channelName
            enableVibration(true)
            vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        }
        return channel
    }


}