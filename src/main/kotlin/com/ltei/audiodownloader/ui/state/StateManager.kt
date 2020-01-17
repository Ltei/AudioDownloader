package com.ltei.audiodownloader.ui.state

import javafx.scene.layout.Pane

class StateManager(
    private val parent: Pane,
    rootState: State
) {

    val listeners = mutableListOf<Listener>()
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
        listeners.forEach { it.onStateResumed(state) }
        parent.children.add(state.stateView)
    }


    interface Listener {
        fun onStateResumed(state: State)
    }

}