package com.ltei.audiodownloader.misc

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import javafx.scene.control.TextArea

open class JsonEditor(
    val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create()
) : TextArea() {
    init {
        minHeight = 100.0
    }

    fun setJson(json: Any?) {
        text = gson.toJson(json)
    }

    fun getJson(): JsonElement? {
        return try {
            JsonParser.parseString(text)
        } catch (e: Exception) {
            null
        }
    }
}