package dev.awd.whistle

import android.app.Application
import dev.awd.whistle.di.AppModule
import dev.awd.whistle.di.NotificationModule

class WhistleApplication : Application() {
    companion object {
        lateinit var notificationModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        notificationModule = NotificationModule(applicationContext)
    }
}