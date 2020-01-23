package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.misc.getParsedQuery
import com.ltei.audiodownloader.model.audiosource.jamendo.JamendoAlbum
import com.ltei.audiodownloader.model.audiosource.jamendo.JamendoArtist
import com.ltei.audiodownloader.model.audiosource.jamendo.JamendoTrack
import com.ltei.audiodownloader.model.audiosource.soundcloud.SoundCloudArtist
import com.ltei.audiodownloader.model.audiosource.soundcloud.SoundCloudSet
import com.ltei.audiodownloader.model.audiosource.soundcloud.SoundCloudTrack
import com.ltei.audiodownloader.model.audiosource.youtube.YouTubeChannel
import com.ltei.audiodownloader.model.audiosource.youtube.YouTubeVideo
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
                        url.path.startsWith("/watch") -> YouTubeVideo(query.getValue("v"))
                        url.path.startsWith("/channel") -> YouTubeChannel(url.path.split("/")[2])
                        else -> null
                    }
                    host.contains("soundcloud", ignoreCase = true) -> {
                        val split = url.path.split("/")
                        when (split.size) {
                            2 -> SoundCloudArtist(split[1])
                            3 -> SoundCloudTrack(split[1], split[2])
                            4 -> when (split[2]) {
                                 "sets" -> SoundCloudSet(split[1], split[3])
                                else -> null
                            }
                            else -> null
                        }
                    }
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