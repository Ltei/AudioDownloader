package com.ltei.audiodownloader.ui.ovh

import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.ui.UIConstants
import com.ltei.audiodownloader.ui.UIStylizer
import com.ltei.audiodownloader.ui.base.BaseLabel
import javafx.application.Platform
import javafx.scene.control.ProgressBar
import javafx.scene.layout.VBox
import tornadofx.add

class AudioDownloadItemView(override var boundObject: AudioDownload? = null) : VBox(), ObjectViewHolder<AudioDownload> {
    private val sourceLabel = BaseLabel()
    private val fileLabel = BaseLabel()
    private val downloadStateLabel = BaseLabel()
    private val downloadProgressBar = ProgressBar()

    override val rootNode = this

    init {
        UIStylizer.setupCardLayout(this)
        spacing = UIConstants.BASE_SPACING

        add(sourceLabel)
        add(fileLabel)

        add(downloadStateLabel)
        add(downloadProgressBar.apply {
            prefWidth = Double.MAX_VALUE
            progress = 0.0
        })

        updateViewFromObject()
    }

    private var lastPercentText: String? = null
    fun updateViewOnStateChanged() {
        val state = boundObject?.state ?: return

        if (state is AudioDownload.State.InProgress) {
            val ratio = state.progress / state.total.toDouble()
            val percentText = "%.2f".format(100.0 * ratio)
            if (percentText != lastPercentText) {
                val progressText = "%.2f".format(state.progress / 1000f)
                val totalText = "%.2f".format(state.total / 1000f)
                val text = "Download progress : $progressText/$totalText kb ($percentText%)."
                Platform.runLater {
                    downloadStateLabel.text = text
                    downloadProgressBar.isVisible = true
                    downloadProgressBar.progress = ratio
                }
                lastPercentText = percentText
            }
        } else {
            lastPercentText = null

            Platform.runLater {
                when (state) {
                    is AudioDownload.State.Waiting -> {
                        downloadStateLabel.text = "Waiting for download"
                        downloadProgressBar.isVisible = true
                        downloadProgressBar.progress = 0.0
                    }
                    is AudioDownload.State.Starting -> {
                        downloadStateLabel.text = "Download starting..."
                        downloadProgressBar.isVisible = true
                        downloadProgressBar.progress = -1.0
                    }
                    is AudioDownload.State.Finished -> {
                        downloadStateLabel.text = "Downloaded"
                        downloadProgressBar.isVisible = false
                    }
                    else -> throw IllegalStateException()
                }
            }
        }
    }

    override fun updateViewFromObject() {
        updateViewOnStateChanged()
        val obj = boundObject
        Platform.runLater {
            isVisible = obj != null
            sourceLabel.text = obj?.source?.toString()
            fileLabel.text = obj?.outputFile?.absolutePath
        }
    }

}