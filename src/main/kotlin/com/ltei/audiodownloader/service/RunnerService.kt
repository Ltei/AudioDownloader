package com.ltei.audiodownloader.service

object RunnerService {

    inline fun runCatching(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}