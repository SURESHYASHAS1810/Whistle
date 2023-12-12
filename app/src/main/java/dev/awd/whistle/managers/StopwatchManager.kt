package dev.awd.whistle.managers

import androidx.compose.runtime.mutableStateOf
import dev.awd.whistle.utils.pad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class StopwatchManager {

    private var duration = Duration.ZERO
    private lateinit var timer: Timer

    var time = MutableStateFlow(
        TimeItem(
            millis = "00",
            seconds = "00",
            minutes = "00"

        )
    )
        private set
    var stopwatchState = mutableStateOf(StopwatchState.Idle)
        private set
    var lapsList = mutableStateOf(emptyList<LapItem>())
        private set

    fun startStopwatch(onTick: (minutes: String, seconds: String, millis: String) -> Unit) {
        timer = fixedRateTimer(initialDelay = 10L, period = 10L) {
            duration = duration.plus(10.milliseconds)
            updateTimeUnits()
            stopwatchState.value = StopwatchState.Started
            onTick(time.value.minutes, time.value.seconds, time.value.millis)
        }
    }

    fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        stopwatchState.value = StopwatchState.Stopped
    }

    fun resetStopwatch() {
        duration = Duration.ZERO
        updateTimeUnits()
        stopwatchState.value = StopwatchState.Idle
        lapsList.value = emptyList()

    }

    fun addNewLapTime() {
        lapsList.value = lapsList.value + LapItem(
            time = duration.minus(lapsList.value.lastOrNull()?.overallTime ?: Duration.ZERO),
            overallTime = duration,
        )
    }

    private fun updateTimeUnits() {
        duration.toComponents { _, minutesComponent, secondsComponent, millisComponent ->
            time.update {
                it.copy(
                    minutes = minutesComponent.pad(),
                    seconds = secondsComponent.pad(),
                    millis = millisComponent.pad().take(2)
                )
            }


        }
    }
}

enum class StopwatchState {
    Idle, Started, Stopped
}

data class LapItem(
    val time: Duration,
    val overallTime: Duration
)

data class TimeItem(
    val millis: String,
    val seconds: String,
    val minutes: String,
)
