package com.ltei.audiodownloader.ui.stage

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.misc.applyTo
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.base.BaseToggleButton
import com.ltei.audiodownloader.ui.view.ovh.AudioMetadataBuilderView
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import tornadofx.add
import tornadofx.asBackground
import tornadofx.scrollpane
import tornadofx.vbox
import kotlin.concurrent.thread

class CreateDownloadStage(
    defaultFileName: String,
    audioMetadata: AudioMetadata
) : Stage() {

    var result: Result? = null
        private set

    private val loadingView = ProgressIndicator()

    private val fileNameField = TextField(defaultFileName)

    private val metadataView = AudioMetadataBuilderView()

    private val autofillMetadataButton = BaseButton("Autofill Metadata", onMouseClicked = EventHandler {
        setLoadingState(true)
        thread {
            metadataView.boundObject?.autofill()
            Platform.runLater {
                metadataView.updateViewFromObject()
                setLoadingState(false)
            }
        }
    })

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

    private val downloadButton = BaseButton("Download", onMouseClicked = EventHandler {
        result = Result(fileName = fileNameField.text)
        this@CreateDownloadStage.close()
    })

    init {
        setOnCloseRequest { this@CreateDownloadStage.close() }

        title = "Download audio"

        val root = StackPane().apply {
            prefWidth = Double.MAX_VALUE

            scrollpane {
                background = UIColors.BACKGROUND.asBackground()
                prefWidth = Double.MAX_VALUE
                isFitToHeight = true
                isFitToWidth = true

                vbox {
                    background = UIColors.BACKGROUND.asBackground()
                    prefWidth = Double.MAX_VALUE
                    padding = UIConstants.BASE_INSETS
                    spacing = UIConstants.BASE_SPACING

                    vbox {
                        UIStylizer.setupCardLayout(this)
                        spacing = UIConstants.BASE_SPACING

                        add(BaseLabel("Output file name :"))
                        add(fileNameField)
                    }

                    add(BaseLabel("Metadata"))
                    add(metadataView.apply {
                        UIStylizer.setupCardLayout(this)
                        boundObject = audioMetadata
                        updateViewFromObject()
                    })

                    add(autofillMetadataButton)
                    add(storeInfoToggleButton)
                    add(downloadButton)
                }
            }

            add(loadingView)
        }

        Preferences.instance.storeAudioInfo.bindBidirectional(storeInfoToggleButton.isOn)

        scene = Scene(root)
        width = UIConstants.BASE_DIALOG_WIDTH
    }

    init {
        setLoadingState(false)
    }

    private fun setLoadingState(isLoading: Boolean) {
        autofillMetadataButton.isDisable = isLoading
        storeInfoToggleButton.isDisable = isLoading
        downloadButton.isDisable = isLoading
        loadingView.isVisible = isLoading
    }

    override fun close() {
        Preferences.instance.storeAudioInfo.unbindBidirectional(storeInfoToggleButton.isOn)
        super.close()
    }

    data class Result(val fileName: String)

}