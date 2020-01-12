package com.ltei.audiodownloader.model

import com.google.gson.GsonBuilder
import com.ltei.audiodownloader.misc.util.fromJson
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.serializer.AudioDownloadStateAdapter
import com.ltei.audiodownloader.model.serializer.AudioSourceUrlAdapter
import com.ltei.audiodownloader.model.serializer.FileAdapter
import com.ltei.audiodownloader.service.FileService
import java.io.File

class Preferences private constructor(
    var outputDirectory: File = File(System.getProperty("user.home"), "Downloads"),
    var keepScreenOnTop: Boolean = false
) {

    companion object {
        private val file = FileService.getOutputFile("preferences.json")
        private val gson = GsonBuilder()
            .registerTypeAdapter(File::class.java, FileAdapter())
            .registerTypeAdapter(AudioDownload.State::class.java, AudioDownloadStateAdapter())
            .registerTypeAdapter(AudioSourceUrl::class.java, AudioSourceUrlAdapter())
            .setPrettyPrinting()
            .create()

        private var mInstance: Preferences? = null
        val instance: Preferences
            get() {
                if (mInstance == null && file.exists()) {
                    try {
                        mInstance = gson.fromJson<Preferences>(file.readText())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (mInstance == null) {
                    mInstance = Preferences()
                }

                return mInstance!!
            }

        fun save() {
            mInstance?.let { instance ->
                file.writeText(gson.toJson(instance))
            }
        }
    }

}