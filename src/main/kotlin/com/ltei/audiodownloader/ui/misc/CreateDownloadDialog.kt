package com.ltei.audiodownloader.ui.misc

import com.ltei.audiodownloader.ui.base.BaseButton
import com.ltei.audiodownloader.ui.base.BaseToggleButton
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Dialog
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*

class CreateDownloadDialog(
    defaultFileName: String
) : Dialog<CreateDownloadDialog.Result>() {

    private val fileNameField = TextField(defaultFileName)

    private val storeInfoToggleButton = BaseToggleButton { button, isOn ->
        if (isOn) {
            UIColors.GREEN.applyTo(button)
            button.text = "Store info (On)"
        } else {
            UIColors.RED.applyTo(button)
            button.text = "Store info (Off)"
        }
    }.apply {
        UIColors.RED.applyTo(this)
        text = "Store info (Off)"
    }

    init {
        val pane = dialogPane

        val window = pane.scene.window
        window.setOnCloseRequest { window.hide() }

        pane.padding = Insets.EMPTY

        pane.prefWidth = UIConstants.BASE_DIALOG_WIDTH
        window.width = UIConstants.BASE_DIALOG_WIDTH
        width = UIConstants.BASE_DIALOG_WIDTH

        graphic = VBox().apply {
            background = UIColors.RED.asBackground()
            prefWidth = UIConstants.BASE_DIALOG_WIDTH

            vbox {
                background = UIColors.GREEN.asBackground()
                spacing = UIConstants.BASE_SPACING
                padding = UIConstants.BASE_INSETS

                label("Download audio?")
                add(fileNameField)
                add(storeInfoToggleButton)
                add(BaseButton("Download", onMouseClicked = EventHandler {
                    result = Result(fileNameField.text, storeInfoToggleButton.isOn)
                    this@CreateDownloadDialog.close()
                }))
            }
        }
    }

    data class Result(val fileName: String, val storeInfo: Boolean)

}