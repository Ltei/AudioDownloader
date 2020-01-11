package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.misc.DownloaderImpl
import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.AudioSourceUrl
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.ui.base.BaseButton
import com.ltei.audiodownloader.ui.base.BaseLabel
import com.ltei.audiodownloader.ui.ovh.AudioDownloadListView
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.control.TextInputDialog
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import org.schabi.newpipe.extractor.NewPipe
import tornadofx.View
import tornadofx.asBackground
import tornadofx.hbox
import tornadofx.vbox
import java.awt.Desktop
import java.io.File

class RootView : View() {

    private val logger = Logger(RootView::class.java)

    private val audioSourceUrlField = TextField("https://www.youtube.com/watch?v=LwVXkM_YxMg")
    private val sourceLabel = BaseLabel("Source : YouTube")
    private val outputDirectoryLabel =
        BaseLabel("Output directory : ${Preferences.instance.outputDirectory.absolutePath}")
    private val audioDownloadListView = AudioDownloadListView()

    override val root = vbox {
        background = UIColors.BACKGROUND.asBackground()
        prefWidth = 600.0
        prefHeight = 600.0
        spacing = 10.0
        padding = UIConstants.BASE_INSETS

        // Toolbar
        hbox {
            prefWidth = Double.MAX_VALUE

            add(BaseButton("Keep on top (Off)").apply {
                style = UIColors.RED.toTintStyleString()
                setOnMouseClicked {
                    val keepOnTop = !Preferences.instance.keepScreenOnTop
                    Preferences.instance.keepScreenOnTop = keepOnTop
                    primaryStage.isAlwaysOnTop = keepOnTop
                    text = if (keepOnTop) "Keep on top (On)" else "Keep on top (Off)"
                    style = if (keepOnTop) UIColors.GREEN.toTintStyleString() else UIColors.RED.toTintStyleString()
                }
            }.apply {
                prefWidth = 9999.0
            })
        }

        // Output directory
        vbox {
            UIStylizer.setupCardLayout(this)
            spacing = UIConstants.BASE_SPACING

            add(outputDirectoryLabel)

            hbox {
                spacing = 10.0

                val selectButton = BaseButton(
                    "Select",
                    onMouseClicked = EventHandler {
                        val chooser = DirectoryChooser()
                        chooser.initialDirectory = Preferences.instance.outputDirectory
                        val directory = chooser.showDialog(currentWindow)
                        if (directory != null && directory.isDirectory) {
                            Preferences.instance.outputDirectory = directory
                            outputDirectoryLabel.text =
                                "Output directory : ${Preferences.instance.outputDirectory.absolutePath}"
                        }
                    }
                )

                val openButton = BaseButton(
                    "Open",
                    onMouseClicked = EventHandler {
                        Desktop.getDesktop().open(Preferences.instance.outputDirectory)
                    }
                )

                selectButton.maxWidth = Double.MAX_VALUE
                HBox.setHgrow(selectButton, Priority.ALWAYS)

                openButton.maxWidth = Double.MAX_VALUE
                HBox.setHgrow(openButton, Priority.ALWAYS)

                add(selectButton)
                add(openButton)
            }
        }

        // Audio Source
        vbox {
            UIStylizer.setupCardLayout(this)
            spacing = UIConstants.BASE_SPACING

            add(BaseLabel("Audio source url :"))
            add(audioSourceUrlField)
            add(sourceLabel)
        }

        add(BaseButton("Download", onMouseClicked = EventHandler {
            val url = audioSourceUrlField.text
            val audioUrl = AudioSourceUrl.parse(url)
            if (audioUrl != null) {
                val dialog = TextInputDialog(audioUrl.getAudioName() ?: "testAudio")
                primaryStage.isAlwaysOnTop = false
                dialog.showAndWait().ifPresent { name ->
                    if (name.isNotBlank()) {
                        val file = File(Preferences.instance.outputDirectory, "$name.${audioUrl.audioExtension}")
                        val audioDownload = AudioDownload(audioUrl, file)
                        synchronized(Model.instance.audioDownloads) {
                            Model.instance.audioDownloads.add(0, audioDownload)
                        }
                        Application.audioDownloadService.start()
                        audioDownloadListView.updateViewFromObject()
                    }
                }
                primaryStage.isAlwaysOnTop = Preferences.instance.keepScreenOnTop
            }
        }))

        add(audioDownloadListView.apply {
            VBox.setVgrow(this, Priority.ALWAYS)
            prefHeight = 0.0
//            maxHeight = 9999.0
        })
    }

    init {
        instance = this
        NewPipe.init(DownloaderImpl)

        audioSourceUrlField.textProperty().addListener { _, _, newValue ->
            val audioUrl = AudioSourceUrl.parse(newValue)
            sourceLabel.text = "Source : ${audioUrl?.sourceName}"
        }

        audioDownloadListView.boundObject = Model.instance.audioDownloads
        audioDownloadListView.updateViewFromObject()
        Application.audioDownloadService.listeners.add(object : AudioDownloadService.Listener {
            override fun onDownloadStarted(download: AudioDownload) {
                logger.debug("Download starting...")
                audioDownloadListView.findCell(download)?.updateViewOnStateChanged()
            }

            override fun onDownloadProgress(download: AudioDownload, progress: Long, total: Long) {
                logger.debug("Download progress : $progress / $total")
                audioDownloadListView
                    .findCell(download)
                    ?.updateViewOnStateChanged()
            }

            override fun onDownloadFinished(download: AudioDownload) {
                audioDownloadListView.findCell(download)?.updateViewOnStateChanged()
                Alert(Alert.AlertType.CONFIRMATION).apply {
                    title = "Success"
                    contentText = "Audio file downloaded to ${download.outputFile.absolutePath}"
                }.showAndWait()
            }

            override fun onDownloadCanceled(download: AudioDownload) {
                audioDownloadListView.findCell(download)?.updateViewOnStateChanged()
            }
        })
    }

    companion object {
        lateinit var instance: RootView
            private set
    }
}