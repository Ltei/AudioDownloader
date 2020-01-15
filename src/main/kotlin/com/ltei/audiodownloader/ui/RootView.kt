package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.misc.ContinuousTask
import com.ltei.audiodownloader.misc.DownloaderImpl
import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.misc.util.ListUtils
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.OutputDirectoryView
import com.ltei.audiodownloader.ui.view.ovh.AudioDownloadListView
import com.ltei.audiodownloader.ui.res.UIColors
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import com.ltei.audiodownloader.ui.stage.CreateDownloadStage
import com.ltei.audiodownloader.ui.stage.SettingsStage
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Modality
import org.schabi.newpipe.extractor.NewPipe
import tornadofx.View
import tornadofx.asBackground
import tornadofx.stackpane
import tornadofx.vbox
import java.io.File
import kotlin.concurrent.thread


class RootView : View(), AudioDownloadService.Listener {

    private val logger = Logger(RootView::class.java)

    private val audioSourceUrlField = TextField("https://www.youtube.com/watch?v=LwVXkM_YxMg")
    private val sourceLabel = BaseLabel("Source : YouTube")

    private val outputDirectoryView = OutputDirectoryView()

    private val downloadButton = BaseButton("Download", onMouseClicked = EventHandler {
        val url = audioSourceUrlField.text
        val audioUrl = AudioSourceUrl.parse(url)
        if (audioUrl != null) download(audioUrl)
    })

    private val audioDownloadListView = AudioDownloadListView()

    private val loadingView = ProgressIndicator()

    override val root = vbox {
        background = UIColors.BACKGROUND.asBackground()
        prefWidth = UIConstants.ROOT_WIDTH
        prefHeight = UIConstants.ROOT_HEIGHT
        //spacing = UIConstants.BASE_SPACING

        // Toolbar
        vbox {
            background = UIColors.PRIMARY_DARK.asBackground()
            prefWidth = Double.MAX_VALUE
            minHeight = 30.0
            prefHeight = 30.0

            add(BaseButton("Settings", onMouseClicked = EventHandler {
                val dialog = SettingsStage()
                dialog.initOwner(currentStage ?: primaryStage)
                dialog.initModality(Modality.WINDOW_MODAL)
                dialog.showAndWait()
            }))
        }

        stackpane {

            vbox {
                prefWidth = UIConstants.ROOT_WIDTH
                prefHeight = UIConstants.ROOT_HEIGHT
                spacing = UIConstants.BASE_SPACING
                padding = UIConstants.BASE_INSETS

                // Output directory
                add(outputDirectoryView)

                // Audio Source
                vbox {
                    UIStylizer.setupCardLayout(this)
                    spacing = UIConstants.BASE_SPACING

                    add(BaseLabel("Audio source url :"))
                    add(audioSourceUrlField)
                    add(sourceLabel)
                }

                add(downloadButton)

                add(audioDownloadListView.apply {
                    VBox.setVgrow(this, Priority.ALWAYS)
                    prefHeight = 0.0
//            maxHeight = 9999.0
                })
            }

            add(loadingView.apply {
                prefWidth = 50.0
                maxWidth = 50.0
                prefHeight = 50.0
                maxHeight = 50.0
                isVisible = false
            })
        }
    }

    private var currentDownload: AudioDownload? = null
    private var audioDownloadListUpdateTask = ContinuousTask(Application.timer, 100L) {
        currentDownload?.let {
            audioDownloadListView.findCell(it)?.updateViewOnStateChanged()
        }
    }

    init {
        instance = this
        NewPipe.init(DownloaderImpl)

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
    }

    override fun onDelete() {
        super.onDelete()
        AudioDownloadService.listeners.remove(this)
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
                dialog.initOwner(primaryStage)
                dialog.showAndWait()
                val result = dialog.getResult()
                if (result != null) {
                    if (result.fileName.isNotBlank()) {
                        if (Preferences.instance.storeAudioInfo.value) {
                            val infoFile = File(
                                Preferences.instance.outputDirectory.value,
                                "${result.fileName} (Info).json"
                            )
                            val infoJson = Globals.persistenceGson.toJson(info)
                            infoFile.writeText(infoJson)
                        }

                        val file = File(
                            Preferences.instance.outputDirectory.value,
                            "${result.fileName}.${info.format}"
                        )
                        val audioDownload = AudioDownload(audioUrl, file)
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