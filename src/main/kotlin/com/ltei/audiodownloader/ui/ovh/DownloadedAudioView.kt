package com.ltei.audiodownloader.ui.ovh

import com.ltei.audiodownloader.model.DownloadedAudio
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import tornadofx.add

class DownloadedAudioView(override var boundObject: DownloadedAudio? = null) : ObjectViewHolder<DownloadedAudio> {
    private val sourceLabel = Label()
    private val fileLabel = Label()

    override val rootNode: Node = VBox().apply {
        add(sourceLabel)
        add(fileLabel)
    }

    override fun updateViewFromObject() {
        val obj = boundObject
        sourceLabel.text = obj?.source?.toString()
        fileLabel.text = obj?.outputFile?.absolutePath
    }
}