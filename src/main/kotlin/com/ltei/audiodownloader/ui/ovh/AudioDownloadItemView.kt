package com.ltei.audiodownloader.ui.ovh

import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.ui.*
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

//    private var lastPercentText: String? = null
//    private var lastUpdateTime: Long = 0
    fun updateViewOnStateChanged() {
        val state = boundObject?.state ?: return

        if (state is AudioDownload.State.InProgress) {
            val ratio = state.progress / state.total.toDouble()
            val percentText = "%.2f".format(100.0 * ratio)
//            val time = System.currentTimeMillis()
//            if (percentText != lastPercentText && time - lastUpdateTime > 100) {
                val progressText = "%.2f".format(state.progress / 1000f)
                val totalText = "%.2f".format(state.total / 1000f)
                val text = "Download progress : $progressText/$totalText kb ($percentText%)."
                Platform.runLater {
                    downloadStateLabel.text = text
                    downloadProgressBar.progress = ratio
                    UIColors.PRIMARY.applyTo(downloadProgressBar)
                }
//                lastPercentText = percentText
//                lastUpdateTime = time
//            }
        } else {
//            lastPercentText = null
//            lastUpdateTime = 0

            Platform.runLater {
                when (state) {
                    is AudioDownload.State.Waiting -> {
                        downloadStateLabel.text = "Waiting for download"
                        downloadProgressBar.progress = 0.0
                        UIColors.PRIMARY.applyTo(downloadProgressBar)
                    }
                    is AudioDownload.State.Starting -> {
                        downloadStateLabel.text = "Download starting..."
                        downloadProgressBar.progress = -1.0
                        UIColors.PRIMARY.applyTo(downloadProgressBar)
                    }
                    is AudioDownload.State.Finished -> {
                        downloadStateLabel.text = "Downloaded"
                        downloadProgressBar.progress = 1.0
                        UIColors.GREEN.applyTo(downloadProgressBar)
                    }
                    is AudioDownload.State.Canceled -> {
                        downloadStateLabel.text = "Canceled"
                        downloadProgressBar.progress = 1.0
                        UIColors.RED.applyTo(downloadProgressBar)
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