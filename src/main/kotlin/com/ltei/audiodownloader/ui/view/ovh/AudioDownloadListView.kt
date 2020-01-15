package com.ltei.audiodownloader.ui.view.ovh

import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.ui.misc.asObservable
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.ListView
import javafx.scene.control.MenuItem
import javafx.util.Callback
import java.awt.Desktop
import java.util.*

class AudioDownloadListView(
    override var boundObject: List<AudioDownload>? = null
) : ListView<AudioDownload>(),
    ObjectViewHolder<List<AudioDownload>> {

    override val rootNode = this

    private val cells = Collections.newSetFromMap(WeakHashMap<AudioDownloadItemView, Boolean>())

    init {
        val model = Model.instance

        cellFactory = Callback {
            val view = AudioDownloadItemView()
            view.setOnContextMenuRequested { event ->
                val obj = view.boundObject
                if (obj != null) {
                    val state = obj.state
                    val items = mutableListOf<MenuItem>()

                    items.add(MenuItem("Delete").apply {
                        onAction = EventHandler {
                            synchronized(model.audioDownloads) {
                                Model.instance.audioDownloads.remove(obj)
                                if (obj === AudioDownloadService.currentDownload) {
                                    AudioDownloadService.restart()
                                }
                            }
                            this@AudioDownloadListView.items = Model.instance.audioDownloads.asObservable()
                        }
                    })

                    if (state is AudioDownload.State.Finished || state is AudioDownload.State.Canceled) {
                        items.add(MenuItem("Retry").apply {
                            onAction = EventHandler {
                                synchronized(model.audioDownloads) {
                                    Model.instance.audioDownloads.add(0, obj.copy(state = AudioDownload.State.Waiting))
                                }
                                this@AudioDownloadListView.items = Model.instance.audioDownloads.asObservable()
                                AudioDownloadService.start()
                            }
                        })
                    }

                    if (state is AudioDownload.State.Finished && obj.outputFile.exists()) {
                        items.add(MenuItem("Play audio").apply {
                            onAction = EventHandler {
                                Desktop.getDesktop().open(obj.outputFile)
                            }
                        })
                    }

                    if (items.isNotEmpty()) {
                        val menu = ContextMenu()
                        menu.items.addAll(items)
                        menu.show(view, event.screenX, event.screenY)
                    }
                }
            }
            view.prefWidth = 0.0
            cells.add(view)
            view.toListCell()
        }
    }

    override fun updateViewFromObject() {
        val obj = boundObject
        rootNode.items = boundObject?.asObservable()
    }

    fun findCell(item: AudioDownload) = cells.find { it.boundObject === item }

}