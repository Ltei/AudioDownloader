package com.ltei.audiodownloader.model.audiosource

import com.google.gson.annotations.Expose
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import java.io.File
import java.net.URL

abstract class AudioSourceUrl(
    val sourceName: String,
    val rawUrl: String
) {

    @Expose(serialize = false, deserialize = false)
    val info: Lazy<Info> = lazy { loadInfo() }

    abstract fun downloadTo(outputFile: File, interceptor: DownloadProgressInterceptor? = null)
    abstract fun loadInfo(): Info

    data class Info(
        val format: String,
        val title: String? = null,
        val artist: String? = null
    )

    companion object {
        fun parse(rawUrl: String): AudioSourceUrl? {
            if (rawUrl.isBlank()) return null
            return try {
                val url = URL(rawUrl)
                val host = url.host
                val query = parseQuery(url.query)
                when {
                    host.contains("youtube", ignoreCase = true) -> YouTubeAudioSourceUrl(query.getValue("v"))
                    host.contains("soundcloud", ignoreCase = true) -> SoundCloudAudioSourceUrl(rawUrl)
                    rawUrl.endsWith(".mp3") -> RawAudioSourceUrl(rawUrl, "mp3")
                    rawUrl.endsWith(".wav") -> RawAudioSourceUrl(rawUrl, "wav")
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