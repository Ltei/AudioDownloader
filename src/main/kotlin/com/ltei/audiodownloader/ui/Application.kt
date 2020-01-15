package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.misc.util.SystemUtils
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.ui.res.UIConstants
import javafx.stage.Screen
import javafx.stage.Stage
import tornadofx.App
import java.util.*

class Application : App() {

    override val primaryView = RootView::class

    private var mTimer: Timer? = null

    init {
        instance = this
    }

    override fun start(stage: Stage) {
        SystemUtils.setProxy("193.56.47.8", "8080")

        mTimer = Timer()
        Runtime.getRuntime().addShutdownHook(ShutdownHook())
        // Size
        stage.width = UIConstants.ROOT_WIDTH
        stage.height = UIConstants.ROOT_HEIGHT
        // Position
        val sb = Screen.getPrimary().visualBounds
        stage.x = sb.minX + (sb.width - UIConstants.ROOT_WIDTH)
        stage.y = sb.minY + (sb.height - UIConstants.ROOT_HEIGHT) / 2
        // Start
        super.start(stage)
        stage.titleProperty().unbind()
        stage.titleProperty().value = "AudioDownloader"

        stage.isAlwaysOnTop = Preferences.instance.keepScreenOnTop.value
        Preferences.instance.keepScreenOnTop.addListener { _, _, newValue ->
            stage.isAlwaysOnTop = newValue
        }
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