package com.ltei.audiodownloader.service

import com.ltei.audiodownloader.misc.debug.Logger

object ContinuousUpdateService {

    private val logger = Logger(AudioDownloadService::class.java)
    private val loopCooldown: Long = 100L
    private var thread: RunnerThread? = null

    val blocks = mutableListOf<() -> Unit>()

    fun start() {
        if (thread?.isKilled != false) {
            thread = RunnerThread()
            thread?.start()
        }
    }

    fun stop() {
        thread?.kill()
        thread = null
    }

    private class RunnerThread : Thread() {
        var isKilled = false
            private set

        override fun run() {
            var lastTime = System.currentTimeMillis()
            while (!isKilled) {
                for (block in blocks) block.invoke()
                val currentTime = System.currentTimeMillis()
                val sleepTime = lastTime - currentTime + loopCooldown
                if (sleepTime > 0) sleep(sleepTime)
                lastTime = currentTime
            }
        }

        fun kill() {
            isKilled = true
        }
    }

}