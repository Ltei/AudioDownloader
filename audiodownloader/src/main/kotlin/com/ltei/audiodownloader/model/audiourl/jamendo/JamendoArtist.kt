package com.ltei.audiodownloader.model.audiourl.jamendo

import com.ltei.audiodownloader.misc.toFuture
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrlProvider
import com.ltei.audiodownloader.web.jamendo.JamendoClient
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import java.net.URL
import java.util.concurrent.CompletableFuture

data class JamendoArtist(
    val artistId: String
) : AudioSourceUrlProvider {
    override val url: String get() = getUrl(artistId)
    override val label: String get() = getLabel(artistId)

    override fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>> =
        JamendoClient.getTrack(artistId = artistId).toFuture().thenApply { result ->
            result.results.map { JamendoTrack(it.id) }
        }

    companion object {
        val urlPathRegex = Regex("^/artist/(${SoundCloudUtils.permalinkRegex})$")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val artistId = result.groupValues[1]
                JamendoArtist(artistId = artistId)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val artistId = result.groupValues[1]
                getLabel(artistId)
            }
        } else null

        fun getUrl(artistId: String) = "https://www.jamendo.com/artist/$artistId"
        fun getLabel(artistId: String) = "Jamendo artist ($artistId)"

        fun isValidUrlHost(host: String) = host.contains("jamendo", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && urlPathRegex.matches(url.path)
    }
}