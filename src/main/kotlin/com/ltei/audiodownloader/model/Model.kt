package com.ltei.audiodownloader.model

import com.google.gson.GsonBuilder
import com.ltei.audiodownloader.misc.util.fromJson
import com.ltei.audiodownloader.service.FileService

class Model private constructor(
    val audioDownloads: MutableList<AudioDownload> = mutableListOf(
        /*AudioDownload(false, AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, FileService.createTempFile()),
        AudioDownload(true, AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, FileService.createTempFile()),
        AudioDownload(true, AudioSourceUrl.parse("https://www.youtube.com/watch?v=LwVXkM_YxMg")!!, FileService.createTempFile())*/
    )
) {

    companion object {
        private val file = FileService.getOutputFile("model.json")
        private val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        private var mInstance: Model? = null
        val instance: Model
            get() {
                var instance = mInstance

                if (instance == null) {
                    instance = try {
                        gson.fromJson<Model>(file.readText())
                    } catch (e: Exception) {
                        Model()
                    }
                    mInstance = instance
                }

                return instance
            }

        fun save() {
            mInstance?.let { instance -> gson.toJson(instance, file.writer()) }
        }
    }

}