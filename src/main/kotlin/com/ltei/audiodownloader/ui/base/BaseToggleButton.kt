package com.ltei.audiodownloader.ui.base

import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler

//class BaseToggleButton(
//    text: String? = null,
//    isOn: Boolean = false,
//    var onToggleEvent: ((button: BaseToggleButton) -> Unit)? = null
//) : BaseButton(text) {
//
//    private var mIsOn: Boolean = isOn
//
//    var isOn: Boolean
//        get() = mIsOn
//        set(value) {
//            if (value != mIsOn) {
//                mIsOn = value
//                onToggleEvent?.invoke(this)
//            }
//        }
//
//    init {
//        onMouseClicked = EventHandler {
//            mIsOn = !mIsOn
//            onToggleEvent?.invoke(this)
//        }
//    }
//}

class BaseToggleButton(
    text: String? = null,
    isOn: Boolean = false
) : BaseButton(text) {

    val isOn = SimpleBooleanProperty(isOn)

    init {
        onMouseClicked = EventHandler {
            this.isOn.value = !this.isOn.value
        }
    }
}