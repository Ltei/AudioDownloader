package com.ltei.audiodownloader.model

import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.misc.util.fromJson
import com.ltei.audiodownloader.service.FileService
import com.ltei.audiodownloader.service.RunnerService

class Model private constructor(
    val audioDownloads: MutableList<AudioDownload> = mutableListOf()
) {

    companion object {
        private val file = FileService.getPersistenceFile("model.json")
        private val gson = Globals.persistenceGson

        private var mInstance: Model? = null
        val instance: Model
            get() {
                if (mInstance == null && file.exists()) {
                    RunnerService.runHandling {
                        mInstance = gson.fromJson<Model>(file.readText())
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