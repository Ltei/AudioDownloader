package com.ltei.audiodownloader.model.audiourl

import com.ltei.audiodownloader.misc.transferTo
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import com.ltei.audiodownloader.service.RunnerService
import org.schabi.newpipe.extractor.stream.AudioStream
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.io.File
import java.net.URL

internal object AudioSourceUtils {

    fun getBestStream(streamInfo: StreamInfo): AudioStream? {
        val streams = streamInfo.audioStreams.sortedByDescending { stream ->
            try {
                val connection = URL(stream.getUrl()).openConnection()
                connection.contentLengthLong
            } catch (e: Exception) {
                -1L
            }
        }
        return streams.firstOrNull()
    }

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
            RunnerService.runHandling {
                downloadRawUrlTo(stream.getUrl(), file, interceptor)
                return
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

    fun getRawUrlFormat(url: String): String? = url.takeLastWhile { it != '.' } // TODO

}