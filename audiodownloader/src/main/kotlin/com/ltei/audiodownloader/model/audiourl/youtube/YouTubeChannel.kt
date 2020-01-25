package com.ltei.audiodownloader.model.audiourl.youtube

import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrlProvider
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import java.net.URL
import java.util.concurrent.CompletableFuture

data class YouTubeChannel(
    val channelId: String
) : AudioSourceUrlProvider {
    override val url: String get() = getUrl(channelId)
    override val label: String get() = getLabel(channelId)

    override fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>> {
        TODO()
    }

    companion object {
        val urlPathRegex = Regex("^/channel/(${SoundCloudUtils.permalinkRegex})")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val channelId = result.groupValues[1]
                YouTubeChannel(channelId = channelId)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val channelId = result.groupValues[1]
                getLabel(channelId = channelId)
            }
        } else null

        fun getUrl(channelId: String) = "https://www.youtube.com/channel/$channelId"
        fun getLabel(channelId: String) = "YouTube channel ($channelId)"

        fun isValidUrlHost(host: String) = host.contains("youtube", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && urlPathRegex.matches(url.path)
    }
}