package com.ltei.audiodownloader.model.audiourl.youtube

import com.github.kiulian.downloader.model.YoutubeVideo
import com.github.kiulian.downloader.model.formats.AudioFormat
import com.github.kiulian.downloader.model.quality.AudioQuality
import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.misc.getParsedQuery
import com.ltei.audiodownloader.model.audiometadata.AudioMetadata
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.DownloadableAudioUrl
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrlProvider
import java.net.URL
import java.util.concurrent.CompletableFuture

class YouTubeVideo(
    val videoId: String
) : AudioSourceUrlProvider, AudioSourceUrl {

    override val label get() = "YouTube video ($videoId)"
    override val url: String = "www.youtube.com/watch?v=$videoId"
    override fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>> = CompletableFuture.completedFuture(listOf<AudioSourceUrl>(this))

    override fun getDownloadableUrl(): CompletableFuture<DownloadableAudioUrl> = CompletableFuture.supplyAsync {
        val video = Globals.youTubeDownloader.getVideo(videoId)
        val format = video.getBestFormat()!!
        DownloadableAudioUrl.Impl(
            url = format.url(),
            format = format.extension()?.value()?.let { if (it == "mp4") "mp3" else it } ?: "mp3"
        )
    }

    override fun getMetadata(): CompletableFuture<AudioMetadata> = CompletableFuture.supplyAsync {
        val video = Globals.youTubeDownloader.getVideo(videoId)
        AudioMetadata(
            title = video.details().title(),
            tags = video.details().keywords()
        )
    }

    override fun getDownloadableUrlAndMetadata(): CompletableFuture<Pair<DownloadableAudioUrl, AudioMetadata>> =
        CompletableFuture.supplyAsync {
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

            Pair(downloadableUrl, metadata)
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

    companion object {
        fun parse(url: URL) = if (isValidUrl(url)) {
            YouTubeVideo(videoId = url.getParsedQuery().getValue("v"))
        } else null

        fun parseLabel(url: URL) = if (isValidUrl(url)) {
            getLabel(videoId = url.getParsedQuery().getValue("v"))
        } else null

        fun getUrl(videoId: String) = "https://www.youtube.com/watch?v=$videoId"
        fun getLabel(videoId: String) = "YouTube video ($videoId)"

        fun isValidUrl(url: URL) = url.host.contains("youtube", ignoreCase = true) &&
                url.path == "/watch" &&
                url.getParsedQuery().containsKey("v")
    }
}