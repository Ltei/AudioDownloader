package com.ltei.audiodownloader.ui.state

import com.ltei.audiodownloader.model.DownloadOutputMode
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.Application
import com.ltei.audiodownloader.ui.UIColors
import com.ltei.audiodownloader.ui.UIConstants
import com.ltei.audiodownloader.ui.UIStylizer
import com.ltei.lteijfxutils.state.State
import com.ltei.lteijfxutils.utils.applyTo
import com.ltei.lteijfxutils.utils.asBackground
import com.ltei.lteijfxutils.utils.asObservable
import com.ltei.lteijfxutils.view.BaseButton
import com.ltei.lteijfxutils.view.BaseLabel
import com.ltei.lteijfxutils.view.BaseToggleButton
import javafx.event.EventHandler
import javafx.scene.control.ChoiceBox
import javafx.scene.layout.VBox


class SettingsState : State {

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

    override val stateView = VBox().apply {
        background = UIColors.BACKGROUND.asBackground()
        prefWidth = Double.MAX_VALUE
        prefHeight = 0.0

        children.add(BaseButton("Back", EventHandler {
            Application.stateManager.popState()
        }))

        children.add(VBox().apply {
            padding = UIConstants.BASE_INSETS
            spacing = UIConstants.BASE_SPACING

            children.add(VBox().apply {
                UIStylizer.setupCardLayout(this)
                spacing = UIConstants.BASE_SPACING

                children.add(BaseLabel("Download output mode :"))
                children.add(outputModeChooser.apply {
                    prefWidth = Double.MAX_VALUE
                })
            })

            children.add(keepScreenOnButton)
        })
    }

    override fun onResume() {
        super.onResume()
        outputModeChooser.value = Preferences.instance.downloadOutputMode.value
        Preferences.instance.downloadOutputMode.bindBidirectional(outputModeChooser.valueProperty())
        Preferences.instance.keepScreenOnTop.bindBidirectional(keepScreenOnButton.isOn)
    }

    override fun onPause() {
        super.onPause()
        Preferences.instance.downloadOutputMode.unbindBidirectional(outputModeChooser.valueProperty())
        Preferences.instance.keepScreenOnTop.unbindBidirectional(keepScreenOnButton.isOn)
    }

}