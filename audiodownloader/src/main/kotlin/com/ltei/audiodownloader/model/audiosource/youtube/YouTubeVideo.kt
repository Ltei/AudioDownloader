package com.ltei.audiodownloader.model.audiosource.youtube

import com.github.kiulian.downloader.model.YoutubeVideo
import com.github.kiulian.downloader.model.formats.AudioFormat
import com.github.kiulian.downloader.model.quality.AudioQuality
import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.DownloadableAudioUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl

class YouTubeVideo(
    val videoId: String
) : MultiAudioSourceUrl, AudioSourceUrl {
    override val label get() = "YouTube video"
    override val url: String = "www.youtube.com/watch?v=$videoId"
    override fun getAudios() = listOf(this)

    override fun getDownloadableUrl(): DownloadableAudioUrl {
        val video = Globals.youTubeDownloader.getVideo(videoId)
        val format = video.getBestFormat()!!
        return DownloadableAudioUrl.Impl(
            url = format.url(),
            format = format.extension()?.value()?.let { if (it == "mp4") "mp3" else it } ?: "mp3"
        )
    }

    override fun getMetadata(): AudioMetadata {
        val video = Globals.youTubeDownloader.getVideo(videoId)
        return AudioMetadata(
            title = video.details().title(),
            tags = video.details().keywords()
        )
    }

    override fun getDownloadableUrlAndMetadata(): Pair<DownloadableAudioUrl, AudioMetadata> {
        val video = Globals.youTubeDownloader.getVideo(videoId)
        val format = video.getBestFormat()!!

        val downloadableUrl = DownloadableAudioUrl.Impl(
            url = format.url(),
            format = format.extension()?.value()?.let { if (it == "mp4") "mp3" else it } ?: "mp3"
        )
        val metadata = AudioMetadata(
            title = video.details().title(),
            tags = video.details().keywords()
        )

        return Pair(downloadableUrl, metadata)
    }

    private fun YoutubeVideo.getBestFormat(): AudioFormat? {
        return audioFormats().maxBy {
            when (it.audioQuality()!!) {
                AudioQuality.unknown -> 0
                AudioQuality.high -> 3
                AudioQuality.medium -> 2
                AudioQuality.low -> 1
                AudioQuality.noAudio -> -1
            }
        }
    }
}