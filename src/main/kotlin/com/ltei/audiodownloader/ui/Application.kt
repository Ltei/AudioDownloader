package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.misc.util.SystemUtils
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.service.AudioDownloadService
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

        super.start(stage)
        stage.titleProperty().unbind()
        stage.titleProperty().value = "AudioDownloader"

        Runtime.getRuntime().addShutdownHook(ShutdownHook())
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