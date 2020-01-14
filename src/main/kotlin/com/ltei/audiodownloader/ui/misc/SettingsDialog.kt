package com.ltei.audiodownloader.ui.misc

import com.ltei.audiodownloader.model.DownloadOutputMode
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.base.BaseButton
import com.ltei.audiodownloader.ui.base.BaseLabel
import com.ltei.audiodownloader.ui.base.BaseToggleButton
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
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


class SettingsDialog : Stage() {

    private val outputModeSpinner = Spinner<DownloadOutputMode>(DownloadOutputMode.values().toList().asObservable())

    private val keepScreenOnButton = BaseToggleButton(
        "Keep screen on top (Off)",
        isOn = Preferences.instance.keepScreenOnTop.value
    )

    init {
        setOnCloseRequest {
            this@SettingsDialog.close()
        }

        title = "Download audio"

        val root = VBox().apply {
            background = UIColors.BACKGROUND.asBackground()
            prefWidth = UIConstants.BASE_DIALOG_WIDTH
            minHeight = Region.USE_PREF_SIZE
            spacing = UIConstants.BASE_SPACING
            padding = UIConstants.BASE_INSETS

            add(BaseLabel("Settings"))

            add(BaseLabel("Download output mode :"))
            add(outputModeSpinner.apply {
                prefWidth = Double.MAX_VALUE
                valueFactory.value = Preferences.instance.downloadOutputMode.value
            })

            run {
                val updateButtonBlock: (BaseToggleButton) -> Unit = { button ->
                    if (button.isOn.value) {
                        UIColors.GREEN.applyTo(button)
                        button.text = "Keep screen on top (On)"
                    } else {
                        UIColors.RED.applyTo(button)
                        button.text = "Keep screen on top (Off)"
                    }
                }
                add(keepScreenOnButton.also { button ->
                    updateButtonBlock.invoke(button)
                    button.isOn.addListener { _, _, newValue ->
                        updateButtonBlock.invoke(button)
                        isAlwaysOnTop = newValue
                    }
                })
            }

            add(BaseButton("OK", onMouseClicked = EventHandler {
                this@SettingsDialog.close()
            }))
        }

        Preferences.instance.downloadOutputMode.bindBidirectional(outputModeSpinner.valueFactory.valueProperty())
        Preferences.instance.keepScreenOnTop.bindBidirectional(keepScreenOnButton.isOn)

        scene = Scene(Group(root))
    }

    override fun close() {
        Preferences.instance.downloadOutputMode.unbindBidirectional(outputModeSpinner.valueFactory.valueProperty())
        Preferences.instance.keepScreenOnTop.unbindBidirectional(keepScreenOnButton.isOn)
        super.close()
    }
}

//class SettingsDialog : Dialog<Unit>() {
//
//    init {
//        val pane = dialogPane
//        val window = pane.scene.window
//
//        window.setOnCloseRequest {
//            this@SettingsDialog.close()
//        }
//
//        title = "Download audio"
//
//        pane.clear()
//        pane.background = UIColors.BACKGROUND.asBackground()
//        pane.padding = Insets.EMPTY
//        pane.prefWidth = UIConstants.BASE_DIALOG_WIDTH
//        pane.minHeight = Region.USE_PREF_SIZE
//
//        pane.content = VBox().apply {
//            spacing = UIConstants.BASE_SPACING
//            padding = UIConstants.BASE_INSETS
//
//            add(BaseLabel("Settings"))
//
//            add(BaseLabel("Download output mode :"))
//            spinner(DownloadOutputMode.values().toList().asObservable()) {
//                prefWidth = Double.MAX_VALUE
//                Preferences.instance.downloadOutputMode.bind(valueProperty())
//            }
//
//            run {
//                val updateButton: (BaseToggleButton) -> Unit = { button ->
//                    if (button.isOn) {
//                        UIColors.GREEN.applyTo(button)
//                        button.text = "Keep screen on top (On)"
//                    } else {
//                        UIColors.RED.applyTo(button)
//                        button.text = "Keep screen on top (Off)"
//                    }
//                }
//                add(BaseToggleButton(
//                    "Keep screen on top (Off)",
//                    isOn = Preferences.instance.keepScreenOnTop.value,
//                    onToggleEvent = { button ->
//                        updateButton.invoke(button)
//                        Preferences.instance.keepScreenOnTop.value = button.isOn
//                        (dialogPane.scene.window as Stage).isAlwaysOnTop = button.isOn
//                    }
//                ).also { button -> updateButton.invoke(button) })
//            }
//        }
//    }
//
//    data class Result(val fileName: String, val storeInfo: Boolean)
//
//}