package com.ltei.audiodownloader.service

import javafx.application.Platform
import javafx.scene.control.Alert
import java.util.concurrent.*

object RunnerService {

    fun runOnUiThread(block: () -> Unit) {
        if (Platform.isFxApplicationThread()) {
            block()
        } else {
            Platform.runLater(block)
        }
    }

    fun handle(t: Throwable) {
        runOnUiThread { handleOnUI(t) }
    }

    private fun handleOnUI(t: Throwable) {
        Alert(Alert.AlertType.ERROR).apply {
            title = "Uncaught exception"
            headerText = "Uncaught exception"
            contentText = t.toString()
        }.showAndWait()
    }

    inline fun runHandling(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            handle(e)
        }
    }

    @Throws(TimeoutException::class)
    fun <T> runWithTimeout(timeout: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, block: () -> T): T {
        val executor = Executors.newSingleThreadExecutor()
        val future: Future<T> = executor.submit(block)
        executor.shutdown() // This does not cancel the already-scheduled task.
        return try {
            future[timeout, timeUnit]
        } catch (e: TimeoutException) {
            future.cancel(true)
            throw e
        } catch (e: ExecutionException) {
            throw e.cause ?: IllegalArgumentException(e)
        }
    }

}