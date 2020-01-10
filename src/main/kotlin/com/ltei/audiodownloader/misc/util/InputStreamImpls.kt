package com.ltei.audiodownloader.misc.util

import com.ltei.audiodownloader.model.DownloadProgressListener
import java.io.InputStream
import java.io.OutputStream

fun InputStream.transferTo(
    outputStream: OutputStream,
    bufferSize: Int = 1024,
    listener: DownloadProgressListener? = null,
    totalSize: Long = available().toLong()
) {
    val buffer = ByteArray(bufferSize)
    var bytesRead = 0L
    var readSize = read(buffer)
    while (readSize >= 0) {
        outputStream.write(buffer, 0, readSize)
        bytesRead += readSize
        listener?.onProgress(bytesRead, totalSize)
        readSize = read(buffer)
    }
}