package com.ltei.audiodownloader.ui.view.base

import javafx.scene.control.ScrollPane
import javafx.scene.input.ScrollEvent


class BaseVScrollPane : ScrollPane() {
    init {
        addEventFilter(ScrollEvent.ANY) {
            val deltaY = it.deltaY * 10
            val height = content.boundsInLocal.height
            val currentV = vvalue
            vvalue = (currentV - deltaY / height).coerceIn(0.0, 1.0)
        }

//        vbarPolicy = ScrollBarPolicy.NEVER
//        isPannable = true
//        isFitToWidth = true
//        isFitToHeight = true
    }
}