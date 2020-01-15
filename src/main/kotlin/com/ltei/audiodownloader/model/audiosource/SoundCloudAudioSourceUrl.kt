package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import com.ltei.audiodownloader.web.SoundCloudScrapper
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.stream.StreamInfo
import java.io.File

class SoundCloudAudioSourceUrl(
    rawUrl: String
) : AudioSourceUrl(
    "SoundCloud",
    rawUrl
) {
    val format = "mp3"

    override fun downloadTo(
        outputFile: File,
        interceptor: DownloadProgressInterceptor?
    ) {
        val streamInfo = StreamInfo.getInfo(ServiceList.SoundCloud, rawUrl)
        AudioSourceUtils.downloadFromNewPipeStreamInfo(streamInfo, outputFile, interceptor)
    }

    override fun loadInfo(): Info {
        val snippet = SoundCloudScrapper.getAudioInfo(rawUrl)
        return Info(
            format = format,
            metadata = AudioMetadata(
                title = snippet?.title,
                artists = snippet?.artist?.let { listOf(it) } ?: listOf()
            )
        )
    }

    override fun toString(): String = "(SoundCloud) $rawUrl"
}