package com.ltei.audiodownloader.ui.state

import javafx.scene.layout.Pane

class StateManager(
    val parent: Pane,
    val rootState: State
) {

    val backStack = mutableListOf(rootState)

    init {
        setShownState(rootState)
    }

    fun pushState(state: State) {
        backStack.last().let {
            it.onPause()
            parent.children.remove(it.stateView)
            if (it.shouldRemoveFromBackStack(state)) backStack.remove(it)
        }
        backStack.add(state)
        setShownState(state)
    }

    fun popState() {
        backStack.removeAt(backStack.lastIndex).let {
            it.onPause()
            parent.children.remove(it.stateView)
        }
        val state = backStack.last()
        setShownState(state)
    }

    fun removeFromBackStack(state: State) {
        if (backStack.indexOf(state) == backStack.lastIndex) {
            popState()
        } else backStack.remove(state)
    }

    private fun setShownState(state: State) {
        state.onResume()
        parent.children.add(state.stateView)
    }

}