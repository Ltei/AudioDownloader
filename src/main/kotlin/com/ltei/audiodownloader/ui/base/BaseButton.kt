package com.ltei.audiodownloader.ui.base

import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.misc.applyTo
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent

open class BaseButton(
    text: String? = null,
    onMouseClicked: EventHandler<MouseEvent>? = null
) : Button(text) {
    init {
        maxWidth = Double.MAX_VALUE
        UIColors.DEFAULT_BUTTON.applyTo(this)
        if (onMouseClicked != null)
            this.onMouseClicked = onMouseClicked
    }
}