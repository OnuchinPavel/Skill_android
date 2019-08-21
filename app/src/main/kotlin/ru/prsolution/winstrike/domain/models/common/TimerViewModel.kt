package ru.prsolution.winstrike.domain.models.common

import java.util.TimerTask

import ru.prsolution.winstrike.presentation.utils.DefaultTimer

class TimerViewModel {

    lateinit var listener: TimeFinishListener
    var timer: DefaultTimer = DefaultTimer
    var state = TimerStates.STOPPED

    interface TimeFinishListener {
        fun onTimeFinish()
    }

    /**
	 * Resets timers and state. Called from the UI.
	 */
    fun stopButtonClicked() {
        state = TimerStates.STOPPED
        timer.reset()
    }

    /**
	 * Start the timer!
	 */
    fun startButtonClicked() {
        state = TimerStates.STARTED

        val task = object : TimerTask() {
            override fun run() {
                if (state == TimerStates.STARTED) {
                    updateCountdowns()
                }
            }
        }

        // Schedule timer every 100ms to update the counters.
        timer.start(task)
    }

    private fun updateCountdowns() {
        if (state == TimerStates.STOPPED) {
            return
        }

        val elapsed: Long?

        if (state == TimerStates.PAUSED) {
            elapsed = timer.getPausedTime()
        } else {
            elapsed = timer.getElapsedTime()
        }

        // work
        updateWorkCountdowns(elapsed)
    }

    private fun updateWorkCountdowns(elapsed: Long?) {
// 		workoutFinished()
    }
}

enum class TimerStates {
    STOPPED, STARTED, PAUSED
}
