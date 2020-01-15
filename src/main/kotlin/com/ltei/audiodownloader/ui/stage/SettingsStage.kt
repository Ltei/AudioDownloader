package com.ltei.audiodownloader.ui.stage

import com.ltei.audiodownloader.model.DownloadOutputMode
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.base.BaseToggleButton
import com.ltei.audiodownloader.ui.misc.applyTo
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Spinner
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.Stage
import tornadofx.add
import tornadofx.asBackground
import tornadofx.asObservable
import tornadofx.vbox


class SettingsStage : Stage() {

    private val outputModeSpinner = Spinner<DownloadOutputMode>(DownloadOutputMode.values().toList().asObservable())

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
        setOnCloseRequest {
            this@SettingsStage.close()
        }

        title = "Settings"

        val root = VBox().apply {
            background = UIColors.BACKGROUND.asBackground()
            prefWidth = UIConstants.BASE_DIALOG_WIDTH
            minHeight = Region.USE_PREF_SIZE
            spacing = UIConstants.BASE_SPACING
            padding = UIConstants.BASE_INSETS

            vbox {
                UIStylizer.setupCardLayout(this)
                spacing = UIConstants.BASE_SPACING

                add(BaseLabel("Download output mode :"))
                add(outputModeSpinner.apply {
                    prefWidth = Double.MAX_VALUE
                    valueFactory.value = Preferences.instance.downloadOutputMode.value
                })
            }

            add(keepScreenOnButton)

            add(BaseButton("OK", onMouseClicked = EventHandler {
                this@SettingsStage.close()
            }))
        }

        Preferences.instance.downloadOutputMode.bindBidirectional(outputModeSpinner.valueFactory.valueProperty())
        Preferences.instance.keepScreenOnTop.bindBidirectional(keepScreenOnButton.isOn)

        keepScreenOnButton.isOn.addListener { _, _, newValue ->
            isAlwaysOnTop = newValue
        }

        scene = Scene(Group(root))
    }

    override fun close() {
        Preferences.instance.downloadOutputMode.unbindBidirectional(outputModeSpinner.valueFactory.valueProperty())
        Preferences.instance.keepScreenOnTop.unbindBidirectional(keepScreenOnButton.isOn)
        super.close()
    }
}