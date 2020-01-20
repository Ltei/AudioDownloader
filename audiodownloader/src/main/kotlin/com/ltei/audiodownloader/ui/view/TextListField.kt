//package com.ltei.audiodownloader.ui.view
//
//import com.ltei.audiodownloader.misc.util.ListUtils
//import javafx.scene.control.TextField
//
//class TextListField(val separator: String = "; ") : TextField() {
//    var textList: List<String>
//        get() = text.split(separator).map { it.trim() }
//        set(value) {
//            text = ListUtils.format(value, separator = separator)
//        }
//}