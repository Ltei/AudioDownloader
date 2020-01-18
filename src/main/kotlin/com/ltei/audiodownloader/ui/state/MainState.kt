package com.ltei.audiodownloader.ui.state

import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.misc.RecurrentTask
import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.misc.util.ListUtils
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.service.RunnerService
import com.ltei.audiodownloader.ui.Application
import com.ltei.audiodownloader.ui.misc.asBackground
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
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
import kotlin.concurrent.thread

class MainState : State, AudioDownloadService.Listener {

    private val settingsButton = BaseButton("Settings", onMouseClicked = EventHandler {
        Application.stateManager.pushState(SettingsState())
    })

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
            Platform.runLater { audioDownloadListView.updateViewFromObject() }
        }
    }

    override val stateView = VBox().apply {
        background = UIColors.BACKGROUND.asBackground()
        prefWidth = UIConstants.ROOT_WIDTH
        prefHeight = UIConstants.ROOT_HEIGHT

        // Toolbar
        children.add(VBox().apply {
            background = UIColors.BACKGROUND.asBackground()
            prefWidth = Double.MAX_VALUE

            children.add(settingsButton)
        })

        children.add(StackPane().apply {
            prefHeight = 9999.0

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
                    prefHeight = 9999.0
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
        audioSourceUrlField.textProperty().addListener { _, _, newValue ->
            val audioUrl = AudioSourceUrl.parse(newValue)
            sourceLabel.text = "Source : ${audioUrl?.sourceName}"
        }
    }

    override fun onResume() {
        super.onResume()
        AudioDownloadService.listeners.add(this)
        // Update view
        audioDownloadListView.boundObject = Model.instance.audioDownloads
        audioDownloadListView.updateViewFromObject()
        // Focus url field by default
        audioSourceUrlField.requestFocus()
        audioSourceUrlField.selectAll()
    }

    override fun onPause() {
        super.onPause()
        AudioDownloadService.listeners.remove(this)
    }

    private fun download(audioUrl: AudioSourceUrl) {
        setLoadingState(true)
        thread {
            val info = RunnerService.runWithTimeout(15000L, block = {
                audioUrl.info.value
            })
            var outputFileName = info.metadata.title ?: "Unknown title"
            info.metadata.artists?.let { artists -> outputFileName = "${ListUtils.format(artists)} - $outputFileName" }

            Platform.runLater {
                setLoadingState(false)
                val state = CreateDownloadState(outputFileName, info.metadata) { result ->
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
                Application.stateManager.pushState(state)
            }
        }.setUncaughtExceptionHandler { _, exception ->
            Platform.runLater {
                setLoadingState(false)
                RunnerService.handle(exception)
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        settingsButton.isDisable = isLoading
        outputDirectoryView.isInputBlocked = isLoading
        audioSourceUrlField.isDisable = isLoading
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
                Platform.runLater {
                    audioDownloadListView.updateViewFromObject()
                    audioDownloadListUpdateTask.notifyTaskRun()
                }
            }
        }
    }

    // Static

    companion object {
        private val logger = Logger(MainState::class.java)
    }

}