package com.ltei.audiodownloader.ui.ovh

import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.lteijfxutils.misc.ObjectViewHolder
import com.ltei.lteijfxutils.misc.toListCell
import com.ltei.lteijfxutils.utils.asObservable
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.ListView
import javafx.scene.control.MenuItem
import javafx.util.Callback
import java.awt.Desktop

class AudioDownloadListView(
    override var boundObject: List<AudioDownload>? = null
) : ListView<AudioDownload>(),
    ObjectViewHolder<List<AudioDownload>> {

    override val rootNode = this

    init {
        val model = Model.instance

        cellFactory = Callback {
            val view = AudioDownloadItemView()
            view.setOnContextMenuRequested { event ->
                val obj = view.boundObject
                if (obj != null) {
                    val state = obj.state
                    val items = mutableListOf<MenuItem>()

                    if (state is AudioDownload.State.Waiting || state is AudioDownload.State.Starting || state is AudioDownload.State.InProgress) {
                        items.add(MenuItem("Cancel").apply {
                            onAction = EventHandler {
                                synchronized(model.audioDownloads) {
                                    obj.state = AudioDownload.State.Canceled
                                    if (obj === AudioDownloadService.currentDownload) {
                                        AudioDownloadService.restart()
                                    }
                                }
                                updateViewFromObject()
                            }
                        })
                    }

                    if (state is AudioDownload.State.Finished || state is AudioDownload.State.Canceled) {
                        items.add(MenuItem("Retry").apply {
                            onAction = EventHandler {
                                synchronized(model.audioDownloads) {
                                    Model.instance.audioDownloads.add(0, obj.copy(state = AudioDownload.State.Waiting))
                                }
                                updateViewFromObject()
                            }
                        })

                        items.add(MenuItem("Delete").apply {
                            onAction = EventHandler {
                                synchronized(model.audioDownloads) {
                                    Model.instance.audioDownloads.remove(obj)
                                    if (obj === AudioDownloadService.currentDownload) {
                                        AudioDownloadService.restart()
                                    }
                                }
                                updateViewFromObject()
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
            view.toListCell()
        }
    }

    override fun updateViewFromObject() {
        val obj = boundObject
        items = obj?.asObservable()
    }

}