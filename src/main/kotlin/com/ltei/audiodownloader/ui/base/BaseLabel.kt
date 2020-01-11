package com.ltei.audiodownloader.ui.base

import com.ltei.audiodownloader.ui.UIColors
import javafx.scene.control.Label

class BaseLabel(text: String = "") : Label(text) {
    init {
        textFill = UIColors.DEFAULT_TEXT
    }
}