package com.ltei.audiodownloader.misc

import com.ltei.audiodownloader.misc.debug.Logger
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class RecurrentTask(
    private val timer: Timer,
    private val cooldown: Long,
    private val block: () -> Unit
) {
    private val lock = Any()
    private var lastRunTime: Long = 0
    private var currentTask: TaskImpl? = null

    fun requestRun() = synchronized(lock) {
        if (currentTask?.finished == false) return
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastRunTime
        if (deltaTime >= cooldown) {
            val delay = deltaTime - cooldown
            val task = TaskImpl()
            currentTask = task
            timer.schedule(task, delay)
            logger.debug("Scheduling new update task")
        }
    }

    fun notifyTaskRun() {
        lastRunTime = System.currentTimeMillis()
    }

    private val currentTask: AtomicReference<TaskImpl?> = AtomicReference(null)
    private inner class TaskImpl : TimerTask() {
        var finished = false
        override fun run() {
            logger.debug("Starting update task")
            notifyTaskRun()
            block()
            finished = true
            logger.debug("Finishing update task")
        }
    }

    companion object {
        private val logger = Logger(RecurrentTask::class.java)
    }
}