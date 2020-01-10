package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.misc.DownloaderImpl
import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.model.AudioSourceUrl
import com.ltei.audiodownloader.model.DownloadProgressListener
import com.ltei.audiodownloader.model.DownloadedAudio
import com.ltei.audiodownloader.ui.base.BaseButton
import com.ltei.audiodownloader.ui.ovh.DownloadedAudioListView
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.stage.DirectoryChooser
import org.schabi.newpipe.extractor.NewPipe
import tornadofx.View
import tornadofx.label
import tornadofx.vbox
import java.io.File
import kotlin.concurrent.thread

class RootView : View() {

    private val logger = Logger(RootView::class.java)
    private var outputDirectory = File(System.getProperty("user.home"), "Downloads")

    private val downloadStateLabel = Label("Not downloading")
    private val audioSourceUrlField = TextField("https://www.youtube.com/watch?v=LwVXkM_YxMg")
    private val sourceLabel = Label("Source : YouTube")
    private val outputDirectoryLabel = Label("Output directory : ${outputDirectory.absolutePath}")
    private val downloadProgressBar = ProgressBar()
    private val downloadedAudiosView = DownloadedAudioListView()

    override val root = vbox {
        prefWidth = 500.0
        prefHeight = 0.0
        spacing = 10.0
        padding = UIConstants.BASE_INSETS

        var keepOnTop = false
        add(BaseButton("Keep on top (Off)").apply {
            style = UIColors.RED.toTintStyleString()
            setOnMouseClicked {
                keepOnTop = !keepOnTop
                primaryStage.isAlwaysOnTop = keepOnTop
                text = if (keepOnTop) "Keep on top (On)" else "Keep on top (Off)"
                style = if (keepOnTop) UIColors.GREEN.toTintStyleString() else UIColors.RED.toTintStyleString()
            }
        })

        label("Audio source url :")
        add(audioSourceUrlField)
        add(sourceLabel)
        add(outputDirectoryLabel)

        add(
            BaseButton(
                "Select output directory",
                onMouseClicked = EventHandler {
                    val chooser = DirectoryChooser()
                    chooser.initialDirectory = outputDirectory
                    val directory = chooser.showDialog(currentWindow)
                    if (directory != null && directory.isDirectory) {
                        outputDirectory = directory
                        outputDirectoryLabel.text = "Output directory : ${outputDirectory.absolutePath}"
                    }
                })
        )


        add(BaseButton("Download", onMouseClicked = EventHandler {
            val url = audioSourceUrlField.text
            val audioUrl = AudioSourceUrl.parse(url)
            if (audioUrl != null) {
                val dialog = TextInputDialog(audioUrl.getAudioName() ?: "testAudio")
                val wasAlwaysOnTop = primaryStage.isAlwaysOnTop
                primaryStage.isAlwaysOnTop = false
                dialog.showAndWait().ifPresent { name ->
                    if (name.isNotBlank()) {
                        thread {
                            val file = File(outputDirectory, "$name.${audioUrl.audioExtension}")
                            Platform.runLater {
                                downloadStateLabel.text = "Download starting..."
                                downloadProgressBar.progress = -1.0
//                                downloadProgressBar.setIsVisibleAndManaged(true)
                            }
                            audioUrl.downloadTo(file, object : DownloadProgressListener {
                                private var lastPercentText: String? = null
                                override fun onProgress(progress: Long, total: Long) {
                                    val ratio = progress / total.toDouble()
                                    val percentText = "%.2f".format(100.0 * ratio)
                                    if (percentText != lastPercentText) {
                                        val progressText = "%.2f".format(progress / 1000f)
                                        val totalText = "%.2f".format(total / 1000f)
                                        val text = "Download progress : $progressText/$totalText kb ($percentText%)."
                                        Platform.runLater {
                                            downloadStateLabel.text = text
                                            downloadProgressBar.progress = ratio
                                        }
                                        logger.debug(text)
                                        lastPercentText = percentText
                                    }
                                }
                            })
                            Platform.runLater {
                                downloadStateLabel.text = "Not downloading"
                                downloadProgressBar.progress = 0.0
//                                downloadProgressBar.setIsVisibleAndManaged(false)
                                Alert(Alert.AlertType.CONFIRMATION).apply {
                                    title = "Success"
                                    contentText = "Audio file downloaded to ${file.absolutePath}"
                                }.showAndWait()
                            }
                        }
                    }
                }
                primaryStage.isAlwaysOnTop = wasAlwaysOnTop
            }
        }))

        add(downloadStateLabel)
        add(downloadProgressBar.apply {
            prefWidth = Double.MAX_VALUE
            progress = 0.0
        })

        add(downloadedAudiosView)

//        downloadProgressBar.setIsVisibleAndManaged(false)

        audioSourceUrlField.textProperty().addListener { _, _, newValue ->
            val audioUrl = AudioSourceUrl.parse(newValue)
            sourceLabel.text = "Source : ${audioUrl?.sourceName}"
        }

        downloadedAudiosView.boundObject = listOf(
            DownloadedAudio(AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, File("salut.Txt")),
            DownloadedAudio(AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, File("salut.Txt")),
            DownloadedAudio(AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, File("salut.Txt")),
            DownloadedAudio(AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, File("salut.Txt")),
            DownloadedAudio(AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, File("salut.Txt")),
            DownloadedAudio(AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, File("salut.Txt"))
        )
        downloadedAudiosView.updateViewFromObject()
    }

    init {
        instance = this
        NewPipe.init(DownloaderImpl)
    }

    companion object {
        lateinit var instance: RootView
            private set
    }
}