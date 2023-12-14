package dev.awd.whistle.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dev.awd.whistle.MainActivity
import dev.awd.whistle.utils.Constants.ACTION_SERVICE_RESET
import dev.awd.whistle.utils.Constants.ACTION_SERVICE_START
import dev.awd.whistle.utils.Constants.ACTION_SERVICE_STOP
import dev.awd.whistle.utils.Constants.CLICK_REQUEST_CODE
import dev.awd.whistle.utils.Constants.RESET_REQUEST_CODE
import dev.awd.whistle.utils.Constants.RESUME_REQUEST_CODE
import dev.awd.whistle.utils.Constants.STOP_REQUEST_CODE

object ServiceHelper {

    private const val flag =
        PendingIntent.FLAG_IMMUTABLE

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, StopwatchService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_SERVICE_START
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, StopwatchService::class.java).apply {
            action = ACTION_SERVICE_STOP
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, stopIntent, flag
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, StopwatchService::class.java).apply {
            action = ACTION_SERVICE_START
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun resetPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, StopwatchService::class.java).apply {
            action = ACTION_SERVICE_RESET
        }

        return PendingIntent.getService(
            context, RESET_REQUEST_CODE, cancelIntent, flag
        )
    }
}