package com.ltei.audiodownloader.model.audiosource.jamendo

import com.ltei.audiodownloader.misc.toFuture
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.soundcloud.SoundCloudArtist
import com.ltei.audiodownloader.web.jamendo.JamendoClient
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import java.net.URL
import java.util.concurrent.CompletableFuture

data class JamendoAlbum(
    val albumId: String
) : MultiAudioSourceUrl {
    override val url: String get() = getUrl(albumId)
    override val label: String get() = getLabel(albumId)

    override fun getAudios(): CompletableFuture<List<AudioSourceUrl>> = JamendoClient.getTrack(albumId = albumId).toFuture().thenApply { result ->
        result.results.map { JamendoTrack(it.id) }
    }

    companion object {
        val urlPathRegex = Regex("album/(${SoundCloudUtils.permalinkRegex})")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val albumId = result.groupValues[1]
                JamendoAlbum(albumId = albumId)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val albumId = result.groupValues[1]
                getLabel(albumId = albumId)
            }
        } else null

        fun getUrl(albumId: String) = "https://www.jamendo.com/album/$albumId"
        fun getLabel(albumId: String) = "Jamendo album ($albumId)"

        fun isValidUrlHost(host: String) = host.contains("jamendo", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && urlPathRegex.matches(url.path)
    }
}