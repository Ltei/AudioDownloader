package com.ltei.audiodownloader.ui.misc

import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import javafx.scene.layout.Pane

fun Pane.setInputsDisabled(disabled: Boolean) {
    for (child in children) {
        if (child is Pane) {
            child.setInputsDisabled(disabled)
        } else if (child is Button || child is TextField || child is ChoiceBox<*>) {
            child.isDisable = disabled
        }
    }
}