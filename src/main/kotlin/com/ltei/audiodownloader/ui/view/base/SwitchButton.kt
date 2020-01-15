package com.ltei.audiodownloader.ui.view.base

import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ContentDisplay
import javafx.scene.control.Label


class SwitchButton : Label() {
    val switchedOn = SimpleBooleanProperty(true)

    init {
        val switchBtn = Button()
        switchBtn.prefWidth = 40.0
        switchBtn.onAction = EventHandler { switchedOn.set(!switchedOn.get()) }
        graphic = switchBtn
        switchedOn.addListener { _, _, newValue ->
            if (newValue) {
                text = "ON"
                style = "-fx-background-color: green;-fx-text-fill:white;"
                contentDisplay = ContentDisplay.RIGHT
            } else {
                text = "OFF"
                style = "-fx-background-color: grey;-fx-text-fill:black;"
                contentDisplay = ContentDisplay.LEFT
            }
        }
        switchedOn.set(false)
    }
}