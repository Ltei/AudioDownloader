package com.ltei.audiodownloader.model.audiosource.soundcloud

import com.ltei.audiodownloader.misc.toFuture
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import com.ltei.audiodownloader.web.soundcloud.SoundCloudClient
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import java.net.URL
import java.util.concurrent.CompletableFuture

class SoundCloudArtist(
    val permalink: String
) : MultiAudioSourceUrl {
    override val url: String get() = getUrl(permalink)
    override val label: String get() = getLabel(permalink)

    override fun getAudios(): CompletableFuture<List<AudioSourceUrl>> =
        SoundCloudClient.resolveResource(url).toFuture().thenApply { user ->
            user as SoundCloudClient.User
            val tracks = SoundCloudClient.getUserTracks(user.id).toFuture().get()
            tracks.map { SoundCloudTrack(user.permalink, it.permalink) }
        }

    companion object {
        val urlPathRegex = Regex("(${SoundCloudUtils.permalinkRegex})")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val permalink = result.groupValues[1]
                SoundCloudArtist(permalink = permalink)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val permalink = result.groupValues[1]
                getLabel(permalink)
            }
        } else null

        fun getUrl(permalink: String) = "https://soundcloud.com/$permalink"
        fun getLabel(permalink: String) = "SoundCloud artist ($permalink)"

        fun isValidUrlHost(host: String) = host.contains("soundcloud", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && SoundCloudPlaylist.urlPathRegex.matches(url.path)
    }
}