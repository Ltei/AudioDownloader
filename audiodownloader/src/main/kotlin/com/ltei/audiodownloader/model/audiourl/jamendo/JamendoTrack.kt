package com.ltei.audiodownloader.model.audiourl.jamendo

import com.ltei.audiodownloader.misc.toFuture
import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.DownloadableAudioUrl
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrlProvider
import com.ltei.audiodownloader.web.jamendo.JamendoClient
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import java.net.URL
import java.util.concurrent.CompletableFuture

data class JamendoTrack(
    val trackId: String
): AudioSourceUrlProvider,
    AudioSourceUrl {
    override val url: String get() = getUrl(trackId)
    override val label: String get() = getLabel(trackId)
    override fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>> = CompletableFuture.completedFuture(listOf<AudioSourceUrl>(this))

    override fun getDownloadableUrl(): CompletableFuture<DownloadableAudioUrl> =
        getTrackCall().toFuture().thenApply { result ->
        val url = result.results.first().audioDownload
        DownloadableAudioUrl.Impl(url, "mp3")
    }

    override fun getMetadata(): CompletableFuture<AudioMetadata> =
        getTrackCall().toFuture().thenApply { result ->
        val track = result.results.first()
        AudioMetadata(
            title = track.name,
            artists = listOf(track.artistName),
            album = track.albumName
            // releaseDate = track.releaseDate TODO Parse date
        )
    }

    fun getTrackCall() = JamendoClient.getTrack(id = trackId)

    companion object {
        val urlPathRegex = Regex("^/track/(${SoundCloudUtils.permalinkRegex})$")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val trackId = result.groupValues[1]
                JamendoTrack(trackId = trackId)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val trackId = result.groupValues[1]
                getLabel(trackId = trackId)
            }
        } else null

        fun getUrl(trackId: String) = "https://www.jamendo.com/track/$trackId"
        fun getLabel(trackId: String) = "Jamendo track ($trackId)"

        fun isValidUrlHost(host: String) = host.contains("jamendo", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && urlPathRegex.matches(url.path)
    }
}