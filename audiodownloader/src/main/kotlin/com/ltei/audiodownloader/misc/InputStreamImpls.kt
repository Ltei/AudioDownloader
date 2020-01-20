package com.ltei.audiodownloader.misc

import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import java.io.InputStream
import java.io.OutputStream

fun InputStream.transferTo(
    outputStream: OutputStream,
    bufferSize: Int = 1024,
    interceptor: DownloadProgressInterceptor? = null,
    totalSize: Long = available().toLong()
) {
    val buffer = ByteArray(bufferSize)
    var bytesRead = 0L
    if (interceptor != null) {
        interceptor.onProgress(bytesRead, totalSize)
        if (interceptor.shouldStop()) return
    }
    var readSize = read(buffer)
    while (readSize >= 0) {
        outputStream.write(buffer, 0, readSize)
        bytesRead += readSize
        if (interceptor != null) {
            if (readSize > 0) interceptor.onProgress(bytesRead, totalSize)
            if (interceptor.shouldStop()) return
        }
        readSize = read(buffer)
    }
}