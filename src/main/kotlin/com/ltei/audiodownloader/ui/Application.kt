package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.misc.DownloaderImpl
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.service.AudioDownloadService
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