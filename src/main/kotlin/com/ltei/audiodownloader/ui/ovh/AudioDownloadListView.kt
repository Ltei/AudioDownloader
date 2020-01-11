package com.ltei.audiodownloader.ui.ovh

import com.ltei.audiodownloader.model.AudioDownload
import javafx.scene.control.ListView
import javafx.util.Callback
import tornadofx.asObservable
import java.util.*

class AudioDownloadListView(
    override var boundObject: List<AudioDownload>? = null
) : ListView<AudioDownload>(),
    ObjectViewHolder<List<AudioDownload>> {

    override val rootNode = this

    private val cells = Collections.newSetFromMap(WeakHashMap<AudioDownloadItemView, Boolean>())

    init {
        cellFactory = Callback {
            val view = AudioDownloadItemView()
            view.prefWidth = 0.0
            cells.add(view)
            view.toListCell()
        }
    }

    override fun updateViewFromObject() {
        val obj = boundObject
        rootNode.items = boundObject?.asObservable()
    }

    fun findCell(item: AudioDownload) = cells.find { it.boundObject == item }

}