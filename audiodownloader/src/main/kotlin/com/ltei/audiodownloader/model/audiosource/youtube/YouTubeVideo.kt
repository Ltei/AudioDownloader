package com.ltei.audiodownloader.model.audiosource.youtube

import com.github.kiulian.downloader.model.YoutubeVideo
import com.github.kiulian.downloader.model.formats.AudioFormat
import com.github.kiulian.downloader.model.quality.AudioQuality
import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.misc.getParsedQuery
import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.DownloadableAudioUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import java.net.URL
import java.util.concurrent.CompletableFuture

class YouTubeVideo(
    val videoId: String
) : MultiAudioSourceUrl, AudioSourceUrl {

    override val label get() = "YouTube video ($videoId)"
    override val url: String = "www.youtube.com/watch?v=$videoId"
    override fun getAudios() = CompletableFuture.completedFuture(listOf<AudioSourceUrl>(this))

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

        fun isValidUrlHost(host: String) = host.contains("youtube", ignoreCase = true)
        fun isValidUrlPath(path: String) = path == "watch"
        fun isValidUrl(url: URL) = isValidUrlHost(url.host) && isValidUrlPath(url.path) && url.getParsedQuery().containsKey("v")
    }
}