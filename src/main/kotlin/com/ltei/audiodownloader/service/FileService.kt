package com.ltei.audiodownloader.service

import java.io.File

object FileService {
    val USER_HOME_DIRECTORY = File(System.getProperty("user.home"))
    val USER_DOWNLOAD_DIRECTORY = File(USER_HOME_DIRECTORY, "Downloads")

    private val LTEI_DIRECTORY = File(USER_HOME_DIRECTORY, ".ltei")

    private val PERSISTENCE_DIRECTORY = File(LTEI_DIRECTORY, "audiodownloader")

    fun getPersistenceFile(name: String): File = File(PERSISTENCE_DIRECTORY, name)

//    val JAR_DIRECTORY = File(FileService::class.java.protectionDomain.codeSource.location.toURI())
//    private val OUTPUT_DIRECTORY = File("out")
//    private val TEMP_DIRECTORY = File("tmp")
//    private var tempFileCount = 0L
//    fun createTempFile(extension: String = "tmp") = File(TEMP_DIRECTORY, "temp${tempFileCount++}.$extension")
}