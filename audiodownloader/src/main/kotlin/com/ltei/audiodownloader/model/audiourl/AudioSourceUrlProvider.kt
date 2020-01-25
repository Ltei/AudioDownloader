package com.ltei.audiodownloader.model.audiourl

import com.ltei.audiodownloader.model.audiourl.jamendo.JamendoAlbum
import com.ltei.audiodownloader.model.audiourl.jamendo.JamendoArtist
import com.ltei.audiodownloader.model.audiourl.jamendo.JamendoTrack
import com.ltei.audiodownloader.model.audiourl.soundcloud.SoundCloudArtist
import com.ltei.audiodownloader.model.audiourl.soundcloud.SoundCloudPlaylist
import com.ltei.audiodownloader.model.audiourl.soundcloud.SoundCloudTrack
import com.ltei.audiodownloader.model.audiourl.youtube.YouTubeChannel
import com.ltei.audiodownloader.model.audiourl.youtube.YouTubeVideo
import java.net.URL
import java.util.concurrent.CompletableFuture

interface AudioSourceUrlProvider {
    val label: String
    val url: String
    fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>>

    companion object {
        fun parseLabel(rawUrl: String): String? {
            if (rawUrl.isBlank()) return null
            val url = URL(rawUrl)
            return YouTubeVideo.parseLabel(url)
                ?: YouTubeChannel.parseLabel(url)
                ?: SoundCloudArtist.parseLabel(url)
                ?: SoundCloudTrack.parseLabel(url)
                ?: SoundCloudPlaylist.parseLabel(url)
                ?: JamendoTrack.parseLabel(url)
                ?: JamendoAlbum.parseLabel(url)
                ?: JamendoArtist.parseLabel(url)
                ?: RawAudioUrl.parseLabel(url)
        }

        fun parse(rawUrl: String): CompletableFuture<AudioSourceUrlProvider?> {
            if (rawUrl.isBlank()) return CompletableFuture.completedFuture(null)
            return CompletableFuture.supplyAsync {
                val url = URL(rawUrl)
                YouTubeVideo.parse(url)
                    ?: YouTubeChannel.parse(url)
                    ?: SoundCloudArtist.parse(url)
                    ?: SoundCloudTrack.parse(url)
                    ?: SoundCloudPlaylist.parse(url)
                    ?: JamendoTrack.parse(url)
                    ?: JamendoAlbum.parse(url)
                    ?: JamendoArtist.parse(url)
                    ?: RawAudioUrl.parse(url)
            }
        }
    }
}