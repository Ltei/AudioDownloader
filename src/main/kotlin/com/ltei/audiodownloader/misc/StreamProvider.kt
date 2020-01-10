package com.ltei.audiodownloader.misc

import java.io.File
import java.io.InputStream

interface StreamProvider {
    fun tryOpenStream(): InputStream?
    fun openStream(): InputStream = tryOpenStream()!!
}

class FileStreamProvider(val file: File) : StreamProvider {
    override fun tryOpenStream(): InputStream? = file.inputStream()
    override fun openStream(): InputStream = file.inputStream()
}

fun File.toStreamProvider() = FileStreamProvider(this)