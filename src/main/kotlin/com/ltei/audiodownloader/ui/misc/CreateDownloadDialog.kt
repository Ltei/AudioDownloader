package com.ltei.audiodownloader.ui.misc

import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.base.BaseButton
import com.ltei.audiodownloader.ui.base.BaseLabel
import com.ltei.audiodownloader.ui.base.BaseToggleButton
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Dialog
import javafx.scene.control.TextField
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import tornadofx.*

class CreateDownloadDialog(
    defaultFileName: String
) : Dialog<CreateDownloadDialog.Result>() {

    private val fileNameField = TextField(defaultFileName)

    private val storeInfoToggleButton = BaseToggleButton { button ->
        if (button.isOn) {
            UIColors.GREEN.applyTo(button)
            button.text = "Store info (On)"
        } else {
            UIColors.RED.applyTo(button)
            button.text = "Store info (Off)"
        }
        Preferences.instance.storeAudioInfo.value = button.isOn
    }.apply {
        UIColors.RED.applyTo(this)
        text = "Store info (Off)"
        isOn = Preferences.instance.storeAudioInfo.value
    }

    init {
        val pane = dialogPane
        val window = pane.scene.window

        window.setOnCloseRequest { this@CreateDownloadDialog.close() }

        title = "Download audio"

        pane.clear()
        pane.background = UIColors.BACKGROUND.asBackground()
        pane.padding = Insets.EMPTY
        pane.prefWidth = UIConstants.BASE_DIALOG_WIDTH
        pane.minHeight = Region.USE_PREF_SIZE

        pane.content = VBox().apply {
            spacing = UIConstants.BASE_SPACING
            padding = UIConstants.BASE_INSETS

            add(BaseLabel("Output file name :"))
            add(fileNameField)
            add(storeInfoToggleButton)
            add(BaseButton("Download", onMouseClicked = EventHandler {
                result = Result(fileNameField.text, storeInfoToggleButton.isOn)
                this@CreateDownloadDialog.close()
            }))
        }
    }

    data class Result(val fileName: String, val storeInfo: Boolean)

}