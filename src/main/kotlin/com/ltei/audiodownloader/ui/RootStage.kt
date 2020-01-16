package com.ltei.audiodownloader.ui

import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.state.MainState
import com.ltei.audiodownloader.ui.state.StateManager
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Screen
import javafx.stage.Stage

class RootStage : Stage() {

    private val rootView = StackPane()
    private val mStateManager = StateManager(rootView, MainState())

    init {
        mInstance = this

        // Size
        width = UIConstants.ROOT_WIDTH
        height = UIConstants.ROOT_HEIGHT
        // Position
        val sb = Screen.getPrimary().visualBounds
        x = sb.minX + (sb.width - UIConstants.ROOT_WIDTH)
        y = sb.minY + (sb.height - UIConstants.ROOT_HEIGHT) / 2
        // Start
        titleProperty().unbind()
        titleProperty().value = "AudioDownloader"

        isAlwaysOnTop = Preferences.instance.keepScreenOnTop.value
        Preferences.instance.keepScreenOnTop.addListener { _, _, newValue -> isAlwaysOnTop = newValue }

        scene = Scene(rootView)
    }

    companion object {
        private var mInstance: RootStage? = null
        val instance get() = mInstance!!
        val stateManager get() = instance.mStateManager
    }

}