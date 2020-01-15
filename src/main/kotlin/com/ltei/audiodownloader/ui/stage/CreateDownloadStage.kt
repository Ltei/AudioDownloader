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
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import tornadofx.*
import kotlin.concurrent.thread

class CreateDownloadStage(
    defaultFileName: String,
    audioMetadata: AudioMetadata
) : Stage() {

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
        this@CreateDownloadStage.close()
    })

    init {
        setOnCloseRequest { this@CreateDownloadStage.close() }

        title = "Download audio"

        val root = StackPane().apply {
            scrollpane {
                vbox {
                    background = UIColors.BACKGROUND.asBackground()
                    prefWidth = UIConstants.BASE_DIALOG_WIDTH
                    minHeight = Region.USE_PREF_SIZE
                    spacing = UIConstants.BASE_SPACING
                    padding = UIConstants.BASE_INSETS

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

        scene = Scene(Group(root))
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

    fun getResult(): Result? = Result(fileName = fileNameField.text)

    data class Result(val fileName: String)

}