package com.ltei.audiodownloader.ui.view.base

import com.ltei.audiodownloader.ui.res.UIColors
import javafx.scene.control.Label

class BaseLabel(text: String = "") : Label(text) {
    init {
        textFill = UIColors.DEFAULT_TEXT
    }
}