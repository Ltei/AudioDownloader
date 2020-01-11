package com.ltei.audiodownloader.service

import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import com.ltei.audiodownloader.model.Model

class AudioDownloadService {

    private val logger = Logger(AudioDownloadService::class.java)
    private var thread: RunnerThread? = null

    val listeners = mutableListOf<Listener>()

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

    private inner class RunnerThread : Thread() {
        var isKilled = false
            private set

        override fun run() {
            val model = Model.instance
            while (!isKilled) {
                val download = synchronized(model.audioDownloads) {
                    val index = model.audioDownloads.indexOfFirst { it.state is AudioDownload.State.Finished }
                    when {
                        index < 0 -> model.audioDownloads.lastOrNull()
                        index == 0 -> null
                        else -> model.audioDownloads[index - 1]
                    }
                } ?: break

                download.state = AudioDownload.State.Starting
                listeners.forEach { it.onDownloadStarted(download) }
                val progressState = AudioDownload.State.InProgress(-1, -1)
                download.source.downloadTo(download.outputFile, interceptor = object : DownloadProgressInterceptor {
                    override fun shouldStop(): Boolean = isKilled
                    override fun onProgress(progress: Long, total: Long) {
                        progressState.progress = progress
                        progressState.total = total
                        download.state = progressState
                        listeners.forEach { it.onDownloadProgress(download, progress, total) }
                    }
                })
                if (isKilled) {
                    logger.debug("Download finished!")
                    download.state = AudioDownload.State.Finished
                    listeners.forEach { it.onDownloadFinished(download) }
                } else {
                    logger.debug("Download canceled!")
                    download.state = AudioDownload.State.Canceled
                    listeners.forEach { it.onDownloadCanceled(download) }
                }
            }
        }

        fun kill() {
            isKilled = true
        }
    }

    interface Listener {
        fun onDownloadStarted(download: AudioDownload)
        fun onDownloadProgress(download: AudioDownload, progress: Long, total: Long)
        fun onDownloadFinished(download: AudioDownload)
        fun onDownloadCanceled(download: AudioDownload)
    }

}