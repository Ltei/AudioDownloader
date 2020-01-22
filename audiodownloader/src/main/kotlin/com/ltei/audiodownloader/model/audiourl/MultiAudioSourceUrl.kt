package com.ltei.audiodownloader.model.audiourl

import com.ltei.audiodownloader.misc.getParsedQuery
import java.net.URL

interface MultiAudioSourceUrl {
    val label: String
    val url: String
    fun getAudios(): List<AudioSourceUrl>

    companion object {
        fun parse(rawUrl: String): MultiAudioSourceUrl? {
            if (rawUrl.isBlank()) return null
            return try {
                val url = URL(rawUrl)
                val host = url.host
                val query = url.getParsedQuery()
                when {
                    host.contains("youtube", ignoreCase = true) -> YouTubeVideoUrl(query.getValue("v"))
                    host.contains("soundcloud", ignoreCase = true) -> SoundCloudTrackUrl(rawUrl)
                    else -> RawAudioUrl(rawUrl)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}