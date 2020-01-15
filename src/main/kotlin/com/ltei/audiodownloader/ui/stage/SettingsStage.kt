package com.ltei.audiodownloader.ui.stage

import com.ltei.audiodownloader.model.DownloadOutputMode
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.misc.applyTo
import com.ltei.audiodownloader.ui.misc.asBackground
import com.ltei.audiodownloader.ui.misc.asObservable
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.base.BaseToggleButton
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.ChoiceBox
import javafx.scene.layout.VBox
import javafx.stage.Stage


class SettingsStage : Stage() {

    private val outputModeChooser = ChoiceBox<DownloadOutputMode>(DownloadOutputMode.values().toList().asObservable())

    private val keepScreenOnButton = BaseToggleButton(
        isOn = Preferences.instance.keepScreenOnTop.value,
        updateLayout = { button ->
            if (button.isOn.value) {
                UIColors.GREEN.applyTo(button)
                button.text = "Keep screen on top (On)"
            } else {
                UIColors.RED.applyTo(button)
                button.text = "Keep screen on top (Off)"
            }
        }
    )

    init {
        setOnCloseRequest { this@SettingsStage.close() }
        title = "Settings"

        val root = VBox().apply {
            background = UIColors.BACKGROUND.asBackground()
            prefWidth = Double.MAX_VALUE
            prefHeight = 0.0
            spacing = UIConstants.BASE_SPACING
            padding = UIConstants.BASE_INSETS


            children.add(VBox().apply {
                UIStylizer.setupCardLayout(this)
                spacing = UIConstants.BASE_SPACING

                children.add(BaseLabel("Download output mode :"))
                children.add(outputModeChooser.apply {
                    prefWidth = Double.MAX_VALUE
                })
            })

            children.add(keepScreenOnButton)

            children.add(BaseButton("OK", onMouseClicked = EventHandler {
                this@SettingsStage.close()
            }))
        }

        outputModeChooser.value = Preferences.instance.downloadOutputMode.value

        Preferences.instance.downloadOutputMode.bindBidirectional(outputModeChooser.valueProperty())
        Preferences.instance.keepScreenOnTop.bindBidirectional(keepScreenOnButton.isOn)
        keepScreenOnButton.isOn.addListener { _, _, newValue -> isAlwaysOnTop = newValue }

        scene = Scene(root)
        width = UIConstants.BASE_DIALOG_WIDTH
    }

    override fun close() {
        Preferences.instance.downloadOutputMode.unbindBidirectional(outputModeChooser.valueProperty())
        Preferences.instance.keepScreenOnTop.unbindBidirectional(keepScreenOnButton.isOn)
        super.close()
    }
}