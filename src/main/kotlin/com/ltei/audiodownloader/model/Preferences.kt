package com.ltei.audiodownloader.model

import com.google.gson.GsonBuilder
import com.ltei.audiodownloader.misc.util.fromJson
import com.ltei.audiodownloader.service.FileService
import java.io.File

class Preferences private constructor(
    var outputDirectory: File = File(System.getProperty("user.home"), "Downloads"),
    var keepScreenOnTop: Boolean = false
) {

    companion object {
        private val file = FileService.getOutputFile("preferences.json")
        private val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        private var mInstance: Preferences? = null
        val instance: Preferences
            get() {
                var instance = mInstance

                if (instance == null) {
                    instance = try {
                        gson.fromJson<Preferences>(file.readText())
                    } catch (e: Exception) {
                        Preferences()
                    }
                    mInstance = instance
                }

                return instance
            }

        fun save() {
            mInstance?.let { instance ->
                file.writeText(gson.toJson(instance))
            }
        }
    }

}