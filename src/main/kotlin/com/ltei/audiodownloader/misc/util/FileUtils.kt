package com.ltei.audiodownloader.misc.util

import java.io.File

val File.isImageFile get() = FileUtils.imageExtensions.contains(extension)

fun File.existsOrNull() = if (exists()) this else null

object FileUtils {
    val audioExtensions = listOf(Extension.MP3, Extension.WAV)
    val imageExtensions = listOf("jpg", "bmp", "png")

    object Extension {
        const val MP3 = "mp3"
        const val WAV = "wav"
    }
}