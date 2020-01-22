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
                    host.contains("youtube", ignoreCase = true) -> when {
                        url.path.startsWith("/watch") -> YouTubeVideoUrl(query.getValue("v"))
                        url.path.startsWith("/channel") -> YouTubeChannelUrl(url.path.split("/")[2])
                        else -> null
                    }
                    host.contains("soundcloud", ignoreCase = true) -> SoundCloudTrackUrl(rawUrl)
                    host.contains("jamendo", ignoreCase = true) -> when {
                        url.path.startsWith("/track") -> JamendoTrack(url.path.split("/")[2])
                        url.path.startsWith("/album") -> JamendoAlbum(url.path.split("/")[2])
                        url.path.startsWith("/artist") -> JamendoArtist(url.path.split("/")[2])
                        else -> null
                    }
                    else -> RawAudioUrl(rawUrl)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}