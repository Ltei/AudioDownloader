package com.ltei.audiodownloader.ui.view.base

import javafx.event.EventHandler
import javafx.scene.control.ScrollPane

class BaseVScrollPane : ScrollPane() {
    init {
        onScroll = EventHandler {
            val deltaY = it.deltaY * 10
            val height = content.boundsInLocal.height
            this@BaseVScrollPane.vvalue -= deltaY / height
        }
    }
}