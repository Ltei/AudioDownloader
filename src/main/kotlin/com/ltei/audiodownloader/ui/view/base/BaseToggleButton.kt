package com.ltei.audiodownloader.ui.view.base

import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler

class BaseToggleButton(
    text: String? = null,
    isOn: Boolean = false
) : BaseButton(text) {

    val isOn = SimpleBooleanProperty(isOn)

    constructor(
        text: String? = null,
        isOn: Boolean = false,
        updateLayout: (BaseToggleButton) -> Unit
    ) : this(text, isOn) {
        updateLayout(this)
        this.isOn.addListener { _, _, _ -> updateLayout(this) }
    }

    constructor(
        isOn: Boolean = false,
        updateLayout: (BaseToggleButton) -> Unit
    ) : this(null, isOn, updateLayout)

    init {
        onMouseClicked = EventHandler {
            this.isOn.value = !this.isOn.value
        }
    }
}