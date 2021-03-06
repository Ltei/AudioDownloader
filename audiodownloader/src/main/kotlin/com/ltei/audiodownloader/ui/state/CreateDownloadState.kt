package com.ltei.audiodownloader.ui.state

import com.ltei.audiodownloader.model.audiometadata.AudioMetadata
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.Application
import com.ltei.audiodownloader.ui.UIColors
import com.ltei.audiodownloader.ui.UIConstants
import com.ltei.audiodownloader.ui.UIStylizer
import com.ltei.audiodownloader.ui.ovh.AudioMetadataBuilderView
import com.ltei.lteijfxutils.state.State
import com.ltei.lteijfxutils.utils.applyTo
import com.ltei.lteijfxutils.utils.asBackground
import com.ltei.lteijfxutils.view.BaseButton
import com.ltei.lteijfxutils.view.BaseLabel
import com.ltei.lteijfxutils.view.BaseToggleButton
import com.ltei.lteijfxutils.view.BaseVScrollPane
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

    private val autofillMetadataButton = BaseButton("Autofill", onMouseClicked = EventHandler {
        setLoadingState(true)
        thread {
            metadataView.boundObject?.autofill()
            Platform.runLater {
                metadataView.updateViewFromObject()
                setLoadingState(false)
            }
        }
    })

    private val metadataLayout = VBox().apply {
        children.addAll(
            metadataView,
            autofillMetadataButton
        )
    }

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

                    children.add(storeInfoToggleButton)

                    children.add(metadataLayout.apply {
                        UIStylizer.setupCardLayout(this)
                        spacing = UIConstants.BASE_SPACING
                    })

                    children.add(downloadButton)
                })
            }
        })

        children.add(loadingView)
    }


    init {
        setLoadingState(false)

        metadataView.boundObject = audioMetadata
        metadataView.updateViewFromObject()
    }

    // State

    override fun onResume() {
        super.onResume()
        Preferences.instance.storeAudioInfo.bindBidirectional(storeInfoToggleButton.isOn)
        metadataLayout.visibleProperty().bind(Preferences.instance.storeAudioInfo)
        metadataLayout.managedProperty().bind(Preferences.instance.storeAudioInfo)
    }

    override fun onPause() {
        super.onPause()
        Preferences.instance.storeAudioInfo.unbindBidirectional(storeInfoToggleButton.isOn)
        metadataLayout.visibleProperty().unbind()
        metadataLayout.managedProperty().unbind()
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