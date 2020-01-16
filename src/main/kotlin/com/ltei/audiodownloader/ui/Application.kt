package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.misc.DownloaderImpl
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.ui.res.UIConstants
import javafx.scene.Scene
import javafx.stage.Screen
import javafx.stage.Stage
import org.schabi.newpipe.extractor.NewPipe
import java.util.*

class Application : javafx.application.Application() {

    private var mTimer: Timer? = null

    init {
        instance = this
    }

    override fun start(stage: Stage) {
//        SystemUtils.setProxy("193.56.47.8", "8080")

        mTimer = Timer()
        NewPipe.init(DownloaderImpl)
        Runtime.getRuntime().addShutdownHook(ShutdownHook())

        RootStage().show()

//        // Size
//        stage.width = UIConstants.ROOT_WIDTH
//        stage.height = UIConstants.ROOT_HEIGHT
//        // Position
//        val sb = Screen.getPrimary().visualBounds
//        stage.x = sb.minX + (sb.width - UIConstants.ROOT_WIDTH)
//        stage.y = sb.minY + (sb.height - UIConstants.ROOT_HEIGHT) / 2
//        // Start
//        stage.titleProperty().unbind()
//        stage.titleProperty().value = "AudioDownloader"
//
//        stage.isAlwaysOnTop = Preferences.instance.keepScreenOnTop.value
//        Preferences.instance.keepScreenOnTop.addListener { _, _, newValue ->
//            stage.isAlwaysOnTop = newValue
//        }
//
//        stage.scene = Scene(RootView(stage))
//        stage.show()
    }

    override fun stop() {
        super.stop()
        AudioDownloadService.stop()
        Model.save()
        Preferences.save()
        mTimer?.cancel()
    }

    private class ShutdownHook : Thread() {
        override fun run() {
            AudioDownloadService.stop()
            Model.save()
            Preferences.save()
            instance.mTimer?.cancel()
        }
    }

    companion object {
        lateinit var instance: Application
            private set

        val timer get() = instance.mTimer!!
    }
}