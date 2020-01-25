package com.ltei.audiodownloader.model.audiosource.soundcloud

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.AudioSourceUtils
import com.ltei.audiodownloader.model.audiosource.DownloadableAudioUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import com.ltei.audiodownloader.web.soundcloud.SoundCloudUtils
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.net.URL
import java.util.concurrent.CompletableFuture

class SoundCloudTrack(
    val artistPermalink: String,
    val permalink: String
) : MultiAudioSourceUrl, AudioSourceUrl {
    override val url: String get() = getUrl(artistPermalink, permalink)
    override val label: String get() = getLabel(artistPermalink, permalink)
    override fun getAudios() = CompletableFuture.completedFuture(listOf<AudioSourceUrl>(this))

    override fun getDownloadableUrl(): CompletableFuture<DownloadableAudioUrl> = CompletableFuture.supplyAsync {
        val streamInfo = StreamInfo.getInfo(ServiceList.SoundCloud, url)
        val stream = AudioSourceUtils.getBestStream(streamInfo)!!
        DownloadableAudioUrl.Impl(
            url = stream.getUrl(),
            format = "mp3"
        )
    }

    override fun getMetadata(): CompletableFuture<AudioMetadata> = CompletableFuture.supplyAsync { // TODO
        val snippet = SoundCloudUtils.getAudioInfo(url)
        AudioMetadata(
            title = snippet?.title,
            artists = snippet?.artist?.let { listOf(it) } ?: listOf()
        )
    }

    companion object {
        val urlPathRegex = Regex("^/(${SoundCloudUtils.permalinkRegex})/(${SoundCloudUtils.permalinkRegex})$")

        fun parse(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val artistPermalink = result.groupValues[1]
                val permalink = result.groupValues[2]
                SoundCloudTrack(artistPermalink = artistPermalink, permalink = permalink)
            }
        } else null

        fun parseLabel(url: URL) = if (isValidUrlHost(url.host)) {
            urlPathRegex.find(url.path)?.let { result ->
                val artistPermalink = result.groupValues[1]
                val permalink = result.groupValues[2]
                getLabel(artistPermalink, permalink)
            }
        } else null

        fun getUrl(artistPermalink: String, permalink: String) = "https://soundcloud.com/$artistPermalink/$permalink"
        fun getLabel(artistPermalink: String, permalink: String) = "SoundCloud track ($artistPermalink/$permalink)"

        fun isValidUrlHost(host: String) = host.contains("soundcloud", ignoreCase = true)
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && urlPathRegex.matches(url.path)
    }
}