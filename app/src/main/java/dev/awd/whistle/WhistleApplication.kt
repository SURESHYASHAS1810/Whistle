package dev.awd.whistle

import android.app.Application

class WhistleApplication : Application() {
    companion object {
        lateinit var notificationModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        notificationModule = NotificationModule(applicationContext)
    }
}