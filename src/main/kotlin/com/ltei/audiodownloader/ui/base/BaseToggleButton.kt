package com.ltei.audiodownloader.ui.base

import javafx.event.EventHandler

class BaseToggleButton(
    text: String? = null,
    var isOn: Boolean = false,
    var onToggleEvent: ((button: BaseToggleButton, on: Boolean) -> Unit)? = null
) : BaseButton(text) {
    init {
        onMouseClicked = EventHandler {
            isOn = !isOn
            onToggleEvent?.invoke(this, isOn)
        }
    }
}