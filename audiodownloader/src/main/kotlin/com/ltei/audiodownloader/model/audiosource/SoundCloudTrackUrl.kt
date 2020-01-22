package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.web.SoundCloudScrapper
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfo

class SoundCloudTrackUrl(
    override val url: String
) : MultiAudioSourceUrl, AudioSourceUrl {
    override val label get() = "SoundCloud track"
    override fun getAudios() = listOf(this)

    override fun getDownloadableUrl(): DownloadableAudioUrl {
        val streamInfo = StreamInfo.getInfo(ServiceList.SoundCloud, url)
        val stream = AudioSourceUtils.getBestStream(streamInfo)!!
        return DownloadableAudioUrl.Impl(
            url = stream.getUrl(),
            format = "mp3"
        )
    }

    override fun getMetadata(): AudioMetadata {
        val snippet = SoundCloudScrapper.getAudioInfo(url)
        return AudioMetadata(
            title = snippet?.title,
            artists = snippet?.artist?.let { listOf(it) } ?: listOf()
        )
    }
}