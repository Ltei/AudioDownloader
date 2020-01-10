package com.ltei.audiodownloader.ui.ovh

import com.ltei.audiodownloader.model.DownloadedAudio
import javafx.scene.control.ListView
import javafx.util.Callback
import tornadofx.asObservable

class DownloadedAudioListView(
    override var boundObject: List<DownloadedAudio>? = null
) : ListView<DownloadedAudio>(),
    ObjectViewHolder<List<DownloadedAudio>> {

    override val rootNode = this

    init {
        cellFactory = Callback { DownloadedAudioView().toListCell() }
    }

    override fun updateViewFromObject() {
        val obj = boundObject
        rootNode.items = boundObject?.asObservable()
    }
}