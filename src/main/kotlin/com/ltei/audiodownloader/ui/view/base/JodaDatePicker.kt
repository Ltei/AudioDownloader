package com.ltei.audiodownloader.ui.view.base

import javafx.scene.control.DatePicker
import org.joda.time.LocalDate

class JodaDatePicker : DatePicker() {
    var jodaDate: LocalDate?
        get() = value?.let { LocalDate(it) }
        set(value) {
            this.value = if (value != null) java.time.LocalDate.from(value.toDate().toInstant()) else null
        }

}