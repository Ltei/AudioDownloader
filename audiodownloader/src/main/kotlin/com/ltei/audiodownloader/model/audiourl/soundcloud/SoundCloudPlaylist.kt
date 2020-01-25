package com.ltei.audiodownloader.model.audiourl.soundcloud

import com.ltei.audiodownloader.misc.toFuture
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrlProvider
import com.ltei.audiodownloader.web.soundcloud.SoundCloudClient
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import java.net.URL
import java.util.concurrent.CompletableFuture

class SoundCloudPlaylist(
    val artistPermalink: String,
    val permalink: String
) : AudioSourceUrlProvider {
    override val url: String get() = getUrl(artistPermalink, permalink)
    override val label: String get() = getLabel(artistPermalink, permalink)
    override fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>> =
        SoundCloudClient.resolveResource(url).toFuture().thenApply { playlist ->
            playlist as SoundCloudClient.Playlist
            playlist.tracks.map {
                SoundCloudTrack(it.user.permalink, it.permalink)
            }
        }

    companion object {
        val urlPathRegex = Regex("^/(${SoundCloudUtils.permalinkRegex})/sets/(${SoundCloudUtils.permalinkRegex})$")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val artistPermalink = result.groupValues[1]
                val permalink = result.groupValues[2]
                SoundCloudPlaylist(artistPermalink = artistPermalink, permalink = permalink)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val artistPermalink = result.groupValues[1]
                val permalink = result.groupValues[2]
                getLabel(artistPermalink, permalink)
            }
        } else null

        fun getUrl(artistPermalink: String, permalink: String) = "https://soundcloud.com/$artistPermalink/sets/$permalink"
        fun getLabel(artistPermalink: String, permalink: String) = "SoundCloud playlist ($artistPermalink/$permalink)"

        fun isValidUrlHost(host: String) = host.contains("soundcloud", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && urlPathRegex.matches(url.path)
    }
}