package com.ltei.audiodownloader.ui.view.ovh

import javafx.scene.Node
import javafx.scene.control.ListCell

interface ObjectViewHolder<T> {
    val rootNode: Node
    var boundObject: T?
    fun updateViewFromObject()
}

interface ObjectBuilderViewHolder<T>: ObjectViewHolder<T> {
    fun updateObjectFromView()
}

fun <T> ObjectViewHolder<T>.toListCell(): ListCell<T> = ListCellImpl(this)

private class ListCellImpl<T>(val objectViewHolder: ObjectViewHolder<T>) : ListCell<T>() {
    init {
        graphic = objectViewHolder.rootNode
    }

    override fun updateItem(item: T?, empty: Boolean) {
        if (empty && item != null) throw IllegalStateException()
        objectViewHolder.boundObject = item
        objectViewHolder.updateViewFromObject()
    }
}