package com.ltei.audiodownloader.misc

import java.util.*

class ContinuousTask(
    private val timer: Timer,
    private val cooldown: Long,
    private val block: () -> Unit
) {
    private var lastRunTime: Long = 0

    fun requestRun() {
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastRunTime
        if (deltaTime >= cooldown) {
            val delay = deltaTime - cooldown
            timer.schedule(TaskImpl(), delay)
            lastRunTime = currentTime + delay
        }
    }

    fun notifyTaskRun() {
        lastRunTime = System.currentTimeMillis()
    }

    private inner class TaskImpl : TimerTask() {
        override fun run() {
            block()
        }
    }
}