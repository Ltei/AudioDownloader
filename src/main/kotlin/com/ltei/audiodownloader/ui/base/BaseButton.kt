package com.ltei.audiodownloader.ui.base

import com.ltei.audiodownloader.ui.UIColors
import com.ltei.audiodownloader.ui.toTextColorCssStyle
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent

open class BaseButton(
    text: String? = null,
    onMouseClicked: EventHandler<MouseEvent>? = null
) : Button(text) {
    init {
        maxWidth = Double.MAX_VALUE
        style = UIColors.DEFAULT_BUTTON.toTextColorCssStyle()
        if (onMouseClicked != null)
            this.onMouseClicked = onMouseClicked
    }
}