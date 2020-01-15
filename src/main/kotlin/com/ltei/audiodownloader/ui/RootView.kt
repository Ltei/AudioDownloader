package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.misc.RecurrentTask
import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.misc.util.ListUtils
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.ui.misc.asBackground
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import com.ltei.audiodownloader.ui.stage.CreateDownloadStage
import com.ltei.audiodownloader.ui.stage.SettingsStage
import com.ltei.audiodownloader.ui.view.OutputDirectoryView
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.ovh.AudioDownloadListView
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import kotlin.concurrent.thread


class RootView(val stage: Stage) : VBox(), AudioDownloadService.Listener {

    private val logger = Logger(RootView::class.java)

    private val outputDirectoryView = OutputDirectoryView()

    private val audioSourceUrlField = TextField("https://www.youtube.com/watch?v=LwVXkM_YxMg")
    private val sourceLabel = BaseLabel("Source : YouTube")

    private val downloadButton = BaseButton("Download", onMouseClicked = EventHandler {
        val url = audioSourceUrlField.text
        val audioUrl = AudioSourceUrl.parse(url)
        if (audioUrl != null) download(audioUrl)
    })

    private val audioDownloadListView = AudioDownloadListView()

    private val loadingView = ProgressIndicator()

    private var currentDownload: AudioDownload? = null
    private var audioDownloadListUpdateTask = RecurrentTask(Application.timer, 100L) {
        currentDownload?.let {
            audioDownloadListView.findCell(it)?.updateViewOnStateChanged()
        }
    }

    // Init layout

    init {
        background = UIColors.BACKGROUND.asBackground()
        prefWidth = UIConstants.ROOT_WIDTH
        prefHeight = UIConstants.ROOT_HEIGHT

        // Toolbar
        children.add(VBox().apply {
            background = UIColors.RED.asBackground()
            prefWidth = Double.MAX_VALUE

            children.add(BaseButton("Settings", onMouseClicked = EventHandler {
                val dialog = SettingsStage()
                dialog.initOwner(stage)
                dialog.initModality(Modality.WINDOW_MODAL)
                dialog.showAndWait()
            }))
        })

        children.add(StackPane().apply {

            children.add(VBox().apply {
                prefWidth = UIConstants.ROOT_WIDTH
                prefHeight = UIConstants.ROOT_HEIGHT
                spacing = UIConstants.BASE_SPACING
                padding = UIConstants.BASE_INSETS

                children.add(outputDirectoryView.apply {
                    UIStylizer.setupCardLayout(this)
                })

                // Audio Source
                children.add(VBox().apply {
                    UIStylizer.setupCardLayout(this)
                    spacing = UIConstants.BASE_SPACING

                    children.add(BaseLabel("Audio source url :"))
                    children.add(audioSourceUrlField)
                    children.add(sourceLabel)
                })

                children.add(downloadButton)

                children.add(audioDownloadListView.apply {
                    VBox.setVgrow(this, Priority.ALWAYS)
                    prefHeight = 0.0
//            maxHeight = 9999.0
                })
            })

            children.add(loadingView.apply {
                prefWidth = 50.0
                maxWidth = 50.0
                prefHeight = 50.0
                maxHeight = 50.0
                isVisible = false
            })
        })
    }

    // Init data

    init {
        instance = this

        audioSourceUrlField.textProperty().addListener { _, _, newValue ->
            val audioUrl = AudioSourceUrl.parse(newValue)
            sourceLabel.text = "Source : ${audioUrl?.sourceName}"
        }

        AudioDownloadService.listeners.add(this)

        audioDownloadListView.boundObject = Model.instance.audioDownloads
        audioDownloadListView.updateViewFromObject()

        // Focus url field by default
        audioSourceUrlField.requestFocus()
        audioSourceUrlField.selectAll()

        stage.onCloseRequest = EventHandler {
            AudioDownloadService.listeners.remove(this)
            stage.close()
        }
    }

    private fun download(audioUrl: AudioSourceUrl) {
        setLoadingState(true)
        thread {
            val info = audioUrl.info.value
            var title = info.metadata.title ?: "Unknown title"
            info.metadata.artists?.let { artists -> title = "${ListUtils.format(artists)} - $title" }

            Platform.runLater {
                setLoadingState(false)
                val dialog = CreateDownloadStage(title, info.metadata)
                dialog.initOwner(stage)
                dialog.showAndWait()
                val result = dialog.result
                if (result != null) {
                    if (result.fileName.isNotBlank()) {
                        if (Preferences.instance.storeAudioInfo.value) {
                            val infoFile = Preferences.instance.downloadOutputMode.value.getInfoFile(
                                outputDirectory = Preferences.instance.outputDirectory.value,
                                fileName = result.fileName
                            )
                            val infoJson = Globals.persistenceGson.toJson(info)
                            infoFile.writeText(infoJson)
                        }

                        val audioFile = Preferences.instance.downloadOutputMode.value.getAudioFile(
                            outputDirectory = Preferences.instance.outputDirectory.value,
                            fileName = result.fileName,
                            extension = info.format
                        )
                        val audioDownload = AudioDownload(audioUrl, audioFile)
                        synchronized(Model.instance.audioDownloads) {
                            Model.instance.audioDownloads.add(0, audioDownload)
                        }
                        AudioDownloadService.start()
                        audioDownloadListView.updateViewFromObject()
                    }
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        outputDirectoryView.setDisabledState(isLoading)
        downloadButton.isDisable = isLoading
        loadingView.isVisible = isLoading
    }

    // AudioDownloadService.Listener

    override fun onDownloadUpdate(download: AudioDownload?) {
        currentDownload = download
        if (download != null) {
            if (download.state is AudioDownload.State.InProgress) {
                audioDownloadListUpdateTask.requestRun()
            } else {
                audioDownloadListView.findCell(download)?.updateViewOnStateChanged()
                audioDownloadListUpdateTask.notifyTaskRun()
            }
        }
    }

    // Static

    companion object {
        lateinit var instance: RootView
            private set
    }
}