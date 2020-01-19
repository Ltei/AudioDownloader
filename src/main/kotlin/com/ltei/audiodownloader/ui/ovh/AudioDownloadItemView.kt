package com.ltei.audiodownloader.ui.ovh

import com.ltei.ljubase.debug.Logger
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.ui.UIColors
import com.ltei.audiodownloader.ui.UIConstants
import com.ltei.audiodownloader.ui.UIStylizer
import com.ltei.lteijfxutils.misc.ObjectViewHolder
import com.ltei.lteijfxutils.utils.applyTo
import com.ltei.lteijfxutils.view.BaseLabel
import javafx.scene.control.ProgressBar
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox

class AudioDownloadItemView(override var boundObject: AudioDownload? = null) : VBox(), ObjectViewHolder<AudioDownload> {
    private val sourceLabel = BaseLabel()
    private val fileLabel = BaseLabel()
    private val downloadStateLabel = BaseLabel()
    private val downloadProgressBar = ProgressBar()

    override val rootNode = this

    init {
        UIStylizer.setupCardLayout(this)
        spacing = UIConstants.BASE_SPACING / 5.0

        children.add(sourceLabel)
        children.add(fileLabel)

        children.add(StackPane().apply {
            children.add(downloadProgressBar.apply {
                prefWidth = Double.MAX_VALUE
                progress = 0.0
            })
            children.add(downloadStateLabel)
        })

        updateViewFromObject()
    }

    fun updateViewOnStateChanged() {
        val state = boundObject?.state ?: return

        if (state is AudioDownload.State.InProgress) {
            val ratio = state.progress / state.total.toDouble()
            val percentText = "%.2f".format(100.0 * ratio)
            val progressText = "%.2f".format(state.progress / 1000f)
            val totalText = "%.2f".format(state.total / 1000f)
            val text = "Download progress : $progressText/$totalText kb ($percentText%)."
            downloadStateLabel.text = text
            downloadProgressBar.progress = ratio
            UIColors.TEXT_DARK.applyTo(downloadStateLabel)
            UIColors.PRIMARY_LIGHT.applyTo(downloadProgressBar)
        } else {
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

    override fun updateViewFromObject() {
        val obj = boundObject
        if (obj == null) {
            isVisible = false
        } else {
            sourceLabel.text = obj.source.toString()
            fileLabel.text = obj.outputFile.absolutePath
            updateViewOnStateChanged()
            isVisible = true
        }
    }

    companion object {
        private val logger = Logger(AudioDownloadItemView::class.java)
    }

}