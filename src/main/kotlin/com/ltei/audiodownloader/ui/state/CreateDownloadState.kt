package com.ltei.audiodownloader.ui.state

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.Application
import com.ltei.audiodownloader.ui.misc.applyTo
import com.ltei.audiodownloader.ui.misc.asBackground
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.base.BaseToggleButton
import com.ltei.audiodownloader.ui.view.base.BaseVScrollPane
import com.ltei.audiodownloader.ui.view.ovh.AudioMetadataBuilderView
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import kotlin.concurrent.thread

class CreateDownloadState(
    defaultFileName: String,
    audioMetadata: AudioMetadata,
    onDestroy: (result: Result?) -> Unit
) : State {

    private val loadingView = ProgressIndicator()

    private val backButton = BaseButton("Back", EventHandler {
        Application.stateManager.popState()
    })

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

    private val downloadButton = BaseButton("OK", onMouseClicked = EventHandler {
        Application.stateManager.popState()
        onDestroy.invoke(Result(fileName = fileNameField.text))
    })

    override val stateView = StackPane().apply {
        background = UIColors.RED.asBackground()
        prefWidth = Double.MAX_VALUE

        children.add(BaseVScrollPane().apply {
            isFitToWidth = true
            isFitToHeight = true

            content = VBox().apply {
                background = UIColors.BACKGROUND.asBackground()
                prefWidth = Double.MAX_VALUE

                children.add(backButton)

                children.add(VBox().apply {
                    padding = UIConstants.BASE_INSETS
                    spacing = UIConstants.BASE_SPACING

                    children.add(VBox().apply {
                        UIStylizer.setupCardLayout(this)
                        spacing = UIConstants.BASE_SPACING

                        children.add(BaseLabel("Output file name :"))
                        children.add(fileNameField)
                    })

                    children.add(BaseLabel("Metadata"))
                    children.add(metadataView.apply {
                        UIStylizer.setupCardLayout(this)
                        boundObject = audioMetadata
                        updateViewFromObject()
                    })

                    children.add(autofillMetadataButton)
                    children.add(storeInfoToggleButton)
                    children.add(downloadButton)
                })
            }
        })

        children.add(loadingView)
    }


    init {
        setLoadingState(false)
    }

    // State

    override fun onResume() {
        super.onResume()
        Preferences.instance.storeAudioInfo.bindBidirectional(storeInfoToggleButton.isOn)
    }

    override fun onPause() {
        super.onPause()
        Preferences.instance.storeAudioInfo.unbindBidirectional(storeInfoToggleButton.isOn)
    }

    // Private misc

    private fun setLoadingState(isLoading: Boolean) {
        backButton.isDisable = isLoading
        fileNameField.isDisable = isLoading
        metadataView.isInputBlocked = isLoading
        autofillMetadataButton.isDisable = isLoading
        storeInfoToggleButton.isDisable = isLoading
        downloadButton.isDisable = isLoading
        loadingView.isVisible = isLoading
    }

    data class Result(val fileName: String)

}