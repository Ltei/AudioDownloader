package com.ltei.audiodownloader.model

import com.ltei.audiodownloader.misc.util.transferTo
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.io.File
import java.net.URL

sealed class AudioSourceUrl(val sourceName: String, val rawUrl: String, val audioExtension: String) {

    abstract fun getAudioName(): String?

    abstract fun downloadTo(file: File, interceptor: DownloadProgressInterceptor? = null)

    protected fun downloadFromNewPipeStreamInfo(
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
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
        throw IllegalArgumentException()
    }

    protected fun downloadRawUrlTo(url: String, file: File, interceptor: DownloadProgressInterceptor?) {
        val connection = URL(url).openConnection()
        val inputStream = connection.getInputStream()
        val outputStream = file.outputStream()
        inputStream.transferTo(outputStream, interceptor = interceptor, totalSize = connection.contentLengthLong)
        outputStream.close()
    }

    class Raw(rawUrl: String, audioExtension: String) : AudioSourceUrl("Raw", rawUrl, audioExtension) {
        override fun getAudioName(): String? = null
        override fun downloadTo(file: File, interceptor: DownloadProgressInterceptor?) =
            downloadRawUrlTo(rawUrl, file, interceptor)

        override fun toString(): String = "(Raw url) $rawUrl"
    }

    class YouTube(rawUrl: String, val videoId: String) : AudioSourceUrl("YouTube", rawUrl, "mp3") {
        override fun getAudioName(): String? = getVideoSnippet()?.snippet?.title

        fun getVideoSnippet(): com.google.api.services.youtube.model.Video? = null

        override fun downloadTo(file: File, interceptor: DownloadProgressInterceptor?) {
            val streamInfo = StreamInfo.getInfo(ServiceList.YouTube, rawUrl)
            downloadFromNewPipeStreamInfo(streamInfo, file, interceptor)
        }

        override fun toString(): String = "(YouTube) $videoId"
    }

    class SoundCloud(rawUrl: String) : AudioSourceUrl("SoundCloud", rawUrl, "mp3") {
        override fun getAudioName(): String? = null
        override fun downloadTo(file: File, interceptor: DownloadProgressInterceptor?) {
            val streamInfo = StreamInfo.getInfo(ServiceList.SoundCloud, rawUrl)
            downloadFromNewPipeStreamInfo(streamInfo, file, interceptor)
        }

        override fun toString(): String = "(SoundCloud) $rawUrl"
    }

    companion object {
        fun parse(rawUrl: String): AudioSourceUrl? {
            if (rawUrl.isBlank()) return null
            return try {
                val url = URL(rawUrl)
                val host = url.host
                val query = parseQuery(url.query)
                when {
                    host.contains("youtube", ignoreCase = true) -> YouTube(rawUrl, query.getValue("v"))
                    host.contains("soundcloud", ignoreCase = true) -> SoundCloud(rawUrl)
                    rawUrl.endsWith(".mp3") -> Raw(rawUrl, "mp3")
                    rawUrl.endsWith(".wav") -> Raw(rawUrl, "wav")
                    else -> null
                }
            } catch (e: Exception) {
                null
            }
        }

        private fun parseQuery(query: String?): Map<String, String> {
            if (query.isNullOrEmpty()) return mapOf()
            val querySplit = query.split('&')
            val result = mutableMapOf<String, String>()
            for (arg in querySplit) {
                val argSplit = arg.split('=')
                result[argSplit[0]] = argSplit[1]
            }
            return result
        }
    }
}