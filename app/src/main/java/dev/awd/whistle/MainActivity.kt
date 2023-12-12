package dev.awd.whistle

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.awd.whistle.service.ServiceHelper
import dev.awd.whistle.service.StopwatchService
import dev.awd.whistle.ui.theme.WhistleTheme
import dev.awd.whistle.utils.Constants.ACTION_SERVICE_LAP
import dev.awd.whistle.utils.Constants.ACTION_SERVICE_RESET
import dev.awd.whistle.utils.Constants.ACTION_SERVICE_START
import dev.awd.whistle.utils.Constants.ACTION_SERVICE_STOP

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var isBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopwatchService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as StopwatchService.StopwatchBinder
            stopwatchService = binder.getService()
            isBound = true
            Log.i(TAG, "onServiceConnected")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhistleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isBound) {
                        val state by stopwatchService.currentState
                        val time by stopwatchService.time.collectAsState()
                        
                        MainScreen(
                            lapList = stopwatchService.lapsList.value,
                            time = time,
                            stopwatchState = state,
                            onStartClick = {
                                ServiceHelper.triggerForegroundService(
                                    applicationContext,
                                    ACTION_SERVICE_START
                                )
                            },
                            onStopClick = {
                                ServiceHelper.triggerForegroundService(
                                    applicationContext,
                                    ACTION_SERVICE_STOP
                                )
                            },
                            onResumeClick = {
                                ServiceHelper.triggerForegroundService(
                                    applicationContext,
                                    ACTION_SERVICE_START
                                )
                            },
                            onResetClick = {
                                ServiceHelper.triggerForegroundService(
                                    applicationContext,
                                    ACTION_SERVICE_RESET
                                )
                            },
                            onLapClick = {
                                ServiceHelper.triggerForegroundService(
                                    applicationContext,
                                    ACTION_SERVICE_LAP
                                )
                            }
                        )
                    }
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }
}
