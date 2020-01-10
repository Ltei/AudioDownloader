package com.ltei.audiodownloader.service

import java.io.File

object FileService {
    val TEMP_FOLDER = File("tmp").apply { mkdirs() }

    private var tempFileCount = 0L
    fun createTempFile(extension: String = "tmp") = File(TEMP_FOLDER, "temp${tempFileCount++}.$extension")
}