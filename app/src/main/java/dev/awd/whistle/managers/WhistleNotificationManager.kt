package dev.awd.whistle.managers

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dev.awd.whistle.service.ServiceHelper
import dev.awd.whistle.utils.Constants
import dev.awd.whistle.utils.formatTime

class WhistleNotificationManager(
    val context: Context,
    val notificationManager: NotificationManager,
    val notificationBuilder: NotificationCompat.Builder
) {

    fun setUp(onInit: (notificationId: Int, notificationBuilder: Notification) -> Unit) {
        createNotificationChannel()
        onInit(Constants.NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    fun setStopButton() {
        notificationBuilder.mActions[0].apply {
            title = "Stop"
            actionIntent =
                ServiceHelper.stopPendingIntent(context)

        }
        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    fun setResumeButton() {
        notificationBuilder.mActions[0].apply {
            title = "Resume"
            actionIntent =
                ServiceHelper.resumePendingIntent(context)
        }
        notificationManager.notify(Constants.NOTIFICATION_ID, notificationBuilder.build())
    }

    fun updateNotification(minutes: String, seconds: String) {
        notificationManager.notify(
            Constants.NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    minutes = minutes,
                    seconds = seconds,
                )
            ).build()
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    fun cancelNotification(onStopForeground: () -> Unit) {
        notificationManager.cancel(Constants.NOTIFICATION_ID)
        onStopForeground()
    }

}