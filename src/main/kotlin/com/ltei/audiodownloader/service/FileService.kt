package com.ltei.audiodownloader.service

import java.io.File

object FileService {
    val USER_HOME_DIRECTORY = File(System.getProperty("user.home"))
    val JAR_DIRECTORY = File(FileService::class.java.protectionDomain.codeSource.location.toURI())
    val LTEI_DIRECTORY = File(USER_HOME_DIRECTORY, ".ltei")

    val OUTPUT_DIRECTORY = File("out").apply { mkdirs() }
    val TEMP_DIRECTORY = File("tmp").apply { mkdirs() }
    val PERSISTENCE_DIRECTORY = File(LTEI_DIRECTORY, "audiodownloader").apply { mkdirs() }

    fun getPersistenceFile(name: String): File = File(PERSISTENCE_DIRECTORY, name)

    private var tempFileCount = 0L
    fun createTempFile(extension: String = "tmp") = File(TEMP_DIRECTORY, "temp${tempFileCount++}.$extension")
}