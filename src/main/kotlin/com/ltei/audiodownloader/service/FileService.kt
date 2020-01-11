package com.ltei.audiodownloader.service

import java.io.File

object FileService {
    val OUT_FOLDER = File("out").apply { mkdirs() }
    val TEMP_FOLDER = File("tmp").apply { mkdirs() }

    fun getOutputFile(name: String) = File(OUT_FOLDER, name)

    private var tempFileCount = 0L
    fun createTempFile(extension: String = "tmp") = File(TEMP_FOLDER, "temp${tempFileCount++}.$extension")
}