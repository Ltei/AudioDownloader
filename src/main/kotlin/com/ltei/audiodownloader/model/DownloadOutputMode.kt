package com.ltei.audiodownloader.model

import java.io.File

enum class DownloadOutputMode {
    Default {
        override fun getInfoFile(outputDirectory: File, fileName: String) =
            File(outputDirectory, "$fileName.json")

        override fun getAudioFile(outputDirectory: File, fileName: String, extension: String) =
            File(outputDirectory, "$fileName.$extension")
    },
    CreateFolder {
        override fun getInfoFile(outputDirectory: File, fileName: String) =
            File(File(outputDirectory, fileName), "$fileName.json")

        override fun getAudioFile(outputDirectory: File, fileName: String, extension: String) =
            File(File(outputDirectory, fileName), "$fileName.$extension")
    };

    abstract fun getInfoFile(outputDirectory: File, fileName: String): File
    abstract fun getAudioFile(outputDirectory: File, fileName: String, extension: String): File
}