package com.ltei.audiodownloader.model

import com.google.gson.GsonBuilder
import com.ltei.audiodownloader.misc.util.fromJson
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.serializer.AudioDownloadStateAdapter
import com.ltei.audiodownloader.model.serializer.AudioSourceUrlAdapter
import com.ltei.audiodownloader.model.serializer.FileAdapter
import com.ltei.audiodownloader.service.FileService
import java.io.File

class Model private constructor(
    val audioDownloads: MutableList<AudioDownload> = mutableListOf()
) {

    companion object {
        private val file = FileService.getOutputFile("model.json")
        private val gson = GsonBuilder()
            .registerTypeAdapter(File::class.java, FileAdapter())
            .registerTypeAdapter(AudioDownload.State::class.java, AudioDownloadStateAdapter())
            .registerTypeAdapter(AudioSourceUrl::class.java, AudioSourceUrlAdapter())
            .setPrettyPrinting()
            .create()

        private var mInstance: Model? = null
        val instance: Model
            get() {
                if (mInstance == null && file.exists()) {
                    try {
                        mInstance = gson.fromJson<Model>(file.readText())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                if (mInstance == null) {
                    mInstance = Model()
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