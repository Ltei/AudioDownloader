package com.ltei.audiodownloader.model.audiourl.soundcloud

import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrlProvider
import com.ltei.audiodownloader.web.soundcloud.SoundCloudClient
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import java.net.URL
import java.util.concurrent.CompletableFuture

class SoundCloudArtist(
    val permalink: String
) : AudioSourceUrlProvider {
    override val url: String get() = getUrl(permalink)
    override val label: String get() = getLabel(permalink)

    override fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>> =
        SoundCloudClient.resolveResource<SoundCloudClient.User>(url).thenApply { user ->
            val tracks = SoundCloudClient.getUserTracks(user!!.id).get()
            tracks.map { SoundCloudTrack(user.permalink, it.permalink) }
        }

    companion object {
        val urlPathRegex = Regex("^/(${SoundCloudUtils.permalinkRegex})$")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val permalink = result.groupValues[1]
                SoundCloudArtist(permalink = permalink)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val permalink = result.groupValues[1]
                println(url.path + " - $permalink")
                getLabel(permalink)
            }
        } else null

        fun getUrl(permalink: String) = "https://soundcloud.com/$permalink"
        fun getLabel(permalink: String) = "SoundCloud artist ($permalink)"

        fun isValidUrlHost(host: String) = host.contains("soundcloud", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && urlPathRegex.matches(url.path)
    }
}