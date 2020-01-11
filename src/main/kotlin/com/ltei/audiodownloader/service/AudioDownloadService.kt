package com.ltei.audiodownloader.service

import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.DownloadProgressListener
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.ui.ovh.AudioDownloadListView
import javafx.scene.control.Alert

class AudioDownloadService(val listView: AudioDownloadListView) {

    private val logger = Logger(AudioDownloadService::class.java)
    private var thread: RunnerThread? = null

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
            while (true) {
                val download = synchronized(model.audioDownloads) {
                    val index = model.audioDownloads.indexOfFirst { it.state is AudioDownload.State.Finished }
                    when {
                        index < 0 -> model.audioDownloads.lastOrNull()
                        index == 0 -> null
                        else -> model.audioDownloads[index - 1]
                    }
                } ?: break

                download.state = AudioDownload.State.Starting
                listView.findCell(download)?.updateViewOnStateChanged()
                logger.debug("Download starting...")
                val progressState = AudioDownload.State.InProgress(-1, -1)
                download.source.downloadTo(download.outputFile, listener = object : DownloadProgressListener {
                    override fun shouldStop(): Boolean = isKilled
                    override fun onProgress(progress: Long, total: Long) {
                        logger.debug("Download progress : $progress / $total")
                        progressState.progress = progress
                        progressState.total = total
                        download.state = progressState
                        listView.findCell(download)?.updateViewOnStateChanged()
                    }
                })
                logger.debug("Download finished!")
                download.state = AudioDownload.State.Finished
                listView.findCell(download)?.updateViewOnStateChanged()

                Alert(Alert.AlertType.CONFIRMATION).apply {
                    title = "Success"
                    contentText = "Audio file downloaded to ${download.outputFile.absolutePath}"
                }.showAndWait()
            }
        }

        fun kill() {
            isKilled = true
        }
    }

}