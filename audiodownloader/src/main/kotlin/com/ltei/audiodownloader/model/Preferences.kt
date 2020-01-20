package com.ltei.audiodownloader.model

import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.service.FileService
import com.ltei.audiodownloader.service.RunnerService
import com.ltei.ljugson.fromJson
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import java.io.File

class Preferences private constructor(
    val outputDirectory: ObjectProperty<File> = SimpleObjectProperty(FileService.USER_DOWNLOAD_DIRECTORY),
    val keepScreenOnTop: BooleanProperty = SimpleBooleanProperty(false),
    val storeAudioInfo: BooleanProperty = SimpleBooleanProperty(false),
    val downloadOutputMode: ObjectProperty<DownloadOutputMode> = SimpleObjectProperty(DownloadOutputMode.Default)
) {

    companion object {
        private val file = FileService.getPersistenceFile("preferences.json")
        private val gson = Globals.persistenceGson

        private var mInstance: Preferences? = null
        val instance: Preferences
            get() {
                if (mInstance == null && file.exists()) {
                    RunnerService.runHandling {
                        mInstance = gson.fromJson<Preferences>(file.readText())
                    }
                }

                if (mInstance == null) {
                    mInstance = Preferences()
                }

                return mInstance!!
            }

        fun save() {
            mInstance?.let { instance ->
                file.parentFile.mkdirs()
                val json = gson.toJson(instance)
                file.writeText(json)
            }
        }
    }

}