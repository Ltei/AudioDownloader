package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.misc.util.transferTo
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.io.File
import java.net.URL

object AudioSourceUtils {

    fun downloadFromNewPipeStreamInfo(
        streamInfo: StreamInfo,
        file: File,
        interceptor: DownloadProgressInterceptor?
    ) {
        val streams = streamInfo.audioStreams.sortedByDescending { stream ->
            try {
                val connection = URL(stream.getUrl()).openConnection()
                connection.contentLengthLong
            } catch (e: Exception) {
                -1L
            }
        }
        for (stream in streams) {
            try {
                downloadRawUrlTo(stream.getUrl(), file, interceptor)
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        throw IllegalArgumentException()
    }

    fun downloadRawUrlTo(url: String, file: File, interceptor: DownloadProgressInterceptor?) {
        val connection = URL(url).openConnection()
        val inputStream = connection.getInputStream()
        val outputStream = file.outputStream()
        inputStream.transferTo(
            outputStream,
            interceptor = interceptor,
            totalSize = connection.contentLengthLong
        )
        outputStream.close()
    }

}