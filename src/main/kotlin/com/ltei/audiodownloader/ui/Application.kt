package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.service.ContinuousUpdaterService
import javafx.stage.Stage
import tornadofx.App

class Application : App() {

    override val primaryView = RootView::class

    init {
        instance = this
    }

    override fun start(stage: Stage) {
        super.start(stage)
        stage.titleProperty().unbind()
        stage.titleProperty().value = "AudioDownloader"

//        audioDownloadService.start()
        ContinuousUpdaterService.start()
        Runtime.getRuntime().addShutdownHook(ShutdownHook())
    }

    override fun stop() {
        super.stop()
        audioDownloadService.stop()
        ContinuousUpdaterService.stop()
        Model.save()
        Preferences.save()
    }

    private class ShutdownHook : Thread() {
        override fun run() {
            audioDownloadService.stop()
            ContinuousUpdaterService.stop()
            Model.save()
            Preferences.save()
        }
    }

    companion object {
        lateinit var instance: Application
            private set

        val audioDownloadService = AudioDownloadService()
    }
}