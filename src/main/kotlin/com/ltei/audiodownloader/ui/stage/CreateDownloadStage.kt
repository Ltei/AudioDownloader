package com.ltei.audiodownloader.ui.stage

import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.base.BaseToggleButton
import com.ltei.audiodownloader.ui.misc.applyTo
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.TextField
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.add
import tornadofx.asBackground

class CreateDownloadStage(defaultFileName: String) : Stage() {

    private val fileNameField = TextField(defaultFileName)

    private val storeInfoToggleButton = BaseToggleButton(
        isOn = Preferences.instance.storeAudioInfo.value,
        updateLayout = { button ->
            if (button.isOn.value) {
                UIColors.GREEN.applyTo(button)
                button.text = "Store info (On)"
            } else {
                UIColors.RED.applyTo(button)
                button.text = "Store info (Off)"
            }
        }
    )

    init {
        setOnCloseRequest { this@CreateDownloadStage.close() }

        title = "Download audio"

        val root = VBox().apply {
            background = UIColors.BACKGROUND.asBackground()
            prefWidth = UIConstants.BASE_DIALOG_WIDTH
            minHeight = Region.USE_PREF_SIZE
            spacing = UIConstants.BASE_SPACING
            padding = UIConstants.BASE_INSETS

            add(BaseLabel("Output file name :"))
            add(fileNameField)
            add(storeInfoToggleButton)
            add(BaseButton("Download", onMouseClicked = EventHandler {
                this@CreateDownloadStage.close()
            }))
        }

        Preferences.instance.storeAudioInfo.bindBidirectional(storeInfoToggleButton.isOn)

        scene = Scene(Group(root))
    }

    override fun close() {
        Preferences.instance.storeAudioInfo.unbindBidirectional(storeInfoToggleButton.isOn)
        super.close()
    }

    fun getResult(): Result? = Result(fileName = fileNameField.text)

    data class Result(val fileName: String)

}