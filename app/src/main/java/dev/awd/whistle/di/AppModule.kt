package dev.awd.whistle.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dev.awd.whistle.R
import dev.awd.whistle.service.ServiceHelper
import dev.awd.whistle.utils.Constants.NOTIFICATION_CHANNEL_ID


interface AppModule {
    val notificationManager: NotificationManager
    val notificationBuilder: NotificationCompat.Builder
}

class NotificationModule(private val context: Context) : AppModule {

    override val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("00:00")
            .setSmallIcon(R.drawable.baseline_whistle_24)

            .setOngoing(true)
            .addAction(0, "Stop", ServiceHelper.stopPendingIntent(context))
            .addAction(0, "Reset", ServiceHelper.resetPendingIntent(context))
            .setContentIntent(ServiceHelper.clickPendingIntent(context))
    }
}
