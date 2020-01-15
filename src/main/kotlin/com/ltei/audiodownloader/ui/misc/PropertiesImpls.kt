package com.ltei.audiodownloader.ui.misc

import javafx.beans.Observable
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.beans.value.ObservableValue
import java.util.concurrent.Callable

fun <T> ObservableValue<T>.stringBinding(vararg dependencies: Observable, op: (T?) -> String?): StringBinding
        = Bindings.createStringBinding(Callable { op(value) }, this, *dependencies)
