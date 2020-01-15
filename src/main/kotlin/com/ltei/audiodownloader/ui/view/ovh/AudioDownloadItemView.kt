package com.ltei.audiodownloader.ui.view.ovh

import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.misc.applyTo
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import javafx.application.Platform
import javafx.scene.control.ProgressBar
import javafx.scene.layout.VBox
import tornadofx.add
import tornadofx.stackpane

class AudioDownloadItemView(override var boundObject: AudioDownload? = null) : VBox(), ObjectViewHolder<AudioDownload> {
    private val sourceLabel = BaseLabel()
    private val fileLabel = BaseLabel()
    private val downloadStateLabel = BaseLabel()
    private val downloadProgressBar = ProgressBar()

    override val rootNode = this

    init {
        UIStylizer.setupCardLayout(this)
        spacing = UIConstants.BASE_SPACING / 5.0

        add(sourceLabel)
        add(fileLabel)

        stackpane {
            add(downloadProgressBar.apply {
                prefWidth = Double.MAX_VALUE
                progress = 0.0
            })
            add(downloadStateLabel)
        }

        updateViewFromObject()
    }

//    private var lastPercentText: String? = null
//    private var lastUpdateTime: Long = 0
    fun updateViewOnStateChanged() {
        val state = boundObject?.state ?: return

        if (state is AudioDownload.State.InProgress) {
            val ratio = state.progress / state.total.toDouble()
            val percentText = "%.2f".format(100.0 * ratio)
            val progressText = "%.2f".format(state.progress / 1000f)
            val totalText = "%.2f".format(state.total / 1000f)
            val text = "Download progress : $progressText/$totalText kb ($percentText%)."
            Platform.runLater {
                downloadStateLabel.text = text
                downloadProgressBar.progress = ratio
                UIColors.TEXT_DARK.applyTo(downloadStateLabel)
                UIColors.PRIMARY_LIGHT.applyTo(downloadProgressBar)
            }
        } else {
            Platform.runLater {
                when (state) {
                    is AudioDownload.State.Waiting -> {
                        downloadStateLabel.text = "Waiting for download"
                        downloadProgressBar.progress = 0.0
                        UIColors.TEXT_DARK.applyTo(downloadStateLabel)
                        UIColors.PRIMARY_LIGHT.applyTo(downloadProgressBar)
                    }
                    is AudioDownload.State.Starting -> {
                        downloadStateLabel.text = "Download starting..."
                        downloadProgressBar.progress = -1.0
                        UIColors.TEXT_DARK.applyTo(downloadStateLabel)
                        UIColors.PRIMARY_LIGHT.applyTo(downloadProgressBar)
                    }
                    is AudioDownload.State.Finished -> {
                        downloadStateLabel.text = "Downloaded"
                        downloadProgressBar.progress = 1.0
                        UIColors.TEXT_DARK.applyTo(downloadStateLabel)
                        UIColors.GREEN.applyTo(downloadProgressBar)
                    }
                    is AudioDownload.State.Canceled -> {
                        downloadStateLabel.text = "Canceled"
                        downloadProgressBar.progress = 1.0
                        UIColors.TEXT_DARK.applyTo(downloadStateLabel)
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