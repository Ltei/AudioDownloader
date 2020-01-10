package com.ltei.audiodownloader.misc.util

import javafx.scene.Node

fun Node.setIsVisibleAndManaged(value: Boolean) {
    isVisible = value
    isManaged = value
}