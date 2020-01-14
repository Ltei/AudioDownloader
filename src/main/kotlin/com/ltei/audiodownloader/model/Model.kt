package com.ltei.audiodownloader.model

import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.misc.util.fromJson
import com.ltei.audiodownloader.service.FileService

class Model private constructor(
    val audioDownloads: MutableList<AudioDownload> = mutableListOf()
) {

    companion object {
        private val file = FileService.getOutputFile("model.json")
        private val gson = Globals.persistenceGson

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