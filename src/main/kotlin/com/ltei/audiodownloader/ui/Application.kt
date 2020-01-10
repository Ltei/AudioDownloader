package com.ltei.audiodownloader.ui

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
    }

    companion object {
        lateinit var instance: Application
            private set
    }
}