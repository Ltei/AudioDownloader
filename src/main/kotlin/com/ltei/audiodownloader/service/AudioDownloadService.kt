package com.ltei.audiodownloader.service

import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import com.ltei.audiodownloader.model.Model

object AudioDownloadService {

    private val logger = Logger(AudioDownloadService::class.java)
    val listeners = mutableListOf<Listener>()

    private var thread: DownloadThread? = null
    val currentDownload get() = thread?.download

    fun start() {
        val isKilled = thread?.isKilled
        if (isKilled == null || isKilled == true) {
            thread = createNextDownloadThread()?.apply { start() }
        }
    }

    fun stop() {
        thread?.kill()
        thread = null
    }

    fun restart() {
        thread?.kill()
        thread = createNextDownloadThread()?.apply { start() }
    }

    private fun createNextDownloadThread() = synchronized(Model.instance.audioDownloads) {
        val download = Model.instance.audioDownloads.lastOrNull { it.state is AudioDownload.State.Waiting }
        if (download != null) DownloadThread(download) else null
    }

    private class DownloadThread(val download: AudioDownload) : Thread() {

        private val logger = Logger(DownloadThread::class.java)

        var isKilled = false
            private set

        override fun run() {
            logger.debug("Starting...")
            download.state = AudioDownload.State.Starting
            listeners.forEach { it.onDownloadUpdate(download) }
            val progressState = AudioDownload.State.InProgress(-1, -1)
            download.source.downloadTo(download.outputFile, interceptor = object : DownloadProgressInterceptor {
                override fun shouldStop(): Boolean = isKilled
                override fun onProgress(progress: Long, total: Long) {
                    progressState.progress = progress
                    progressState.total = total
                    download.state = progressState
                    listeners.forEach { it.onDownloadUpdate(download) }
                }
            })
            if (isKilled) {
                logger.debug("Download canceled!")
                download.state = AudioDownload.State.Canceled
            } else {
                logger.debug("Download finished!")
                download.state = AudioDownload.State.Finished
            }
            isKilled = true
            listeners.forEach { it.onDownloadUpdate(download) }
            listeners.forEach { it.onDownloadUpdate(null) }
            logger.debug("Finishing...")
            AudioDownloadService.start()
        }

        fun kill() {
            isKilled = true
        }
    }

    interface Listener {
        fun onDownloadUpdate(download: AudioDownload?)
    }

}