package com.ltei.audiodownloader.ui.state

import javafx.scene.Node

interface State {
    val stateView: Node
    fun shouldRemoveFromBackStack(newState: State) = false

    fun onResume() = Unit
    fun onPause() = Unit
}