package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.misc.DownloaderImpl
import com.ltei.audiodownloader.model.Model
import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.service.AudioDownloadService
import com.ltei.audiodownloader.service.RunnerService
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.state.MainState
import com.ltei.audiodownloader.ui.state.StateManager
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Screen
import javafx.stage.Stage
import org.schabi.newpipe.extractor.NewPipe
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Application : javafx.application.Application() {

    private var mTimer: Timer? = null
    private var mStage: Stage? = null
    private var mStateManager: StateManager? = null

    private var releaseCalled = AtomicBoolean(false)

    init {
        mInstance = this
    }

    override fun start(stage: Stage) {
        // Init
//        SystemUtils.setProxy("193.56.47.8", "8080")
        NewPipe.init(DownloaderImpl)
        Runtime.getRuntime().addShutdownHook(ShutdownHook())

        // Setup stage
        stage.onCloseRequest = EventHandler { exit() }

        stage.width = UIConstants.ROOT_WIDTH
        stage.height = UIConstants.ROOT_HEIGHT

        val sb = Screen.getPrimary().visualBounds
        stage.x = sb.minX + (sb.width - UIConstants.ROOT_WIDTH)
        stage.y = sb.minY + (sb.height - UIConstants.ROOT_HEIGHT) / 2

        stage.titleProperty().unbind()
        stage.titleProperty().value = "AudioDownloader"

        stage.isAlwaysOnTop = Preferences.instance.keepScreenOnTop.value
        Preferences.instance.keepScreenOnTop.addListener { _, _, newValue -> stage.isAlwaysOnTop = newValue }

        // Setup app
        val statePane = StackPane()
        val rootView = statePane
        mTimer = Timer()
        mStage = stage
        mStateManager = StateManager(statePane, MainState())

        // Start
        stage.scene = Scene(rootView)
        stage.show()
    }

    override fun stop() {
        release()
        super.stop()
    }

    fun exit() {
        release()
        Platform.exit()
    }

    private fun release() {
        if (!releaseCalled.getAndSet(true)) {
            RunnerService.runHandlingOnBack {
                Model.save()
                Preferences.save()
            }
            AudioDownloadService.stop()
            mTimer?.cancel()
            mTimer = null
            mStateManager?.backStack?.lastOrNull()?.onPause()
        }
    }

    private class ShutdownHook : Thread() {
        override fun run() {
            mInstance?.release()
        }
    }

    companion object {
        private var mInstance: Application? = null
        val instance get() = mInstance!!

        val timer get() = instance.mTimer!!
        val stage get() = instance.mStage!!
        val stateManager get() = instance.mStateManager!!
    }
}