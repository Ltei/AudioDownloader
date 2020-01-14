package com.ltei.audiodownloader.model.audiosource

import com.github.kiulian.downloader.YoutubeException
import com.github.kiulian.downloader.model.YoutubeVideo
import com.github.kiulian.downloader.model.formats.AudioFormat
import com.github.kiulian.downloader.model.quality.AudioQuality
import com.ltei.audiodownloader.Globals
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class YouTubeAudioSourceUrl(
    val videoId: String
): AudioSourceUrl("YouTube", "www.youtube.com?v=$videoId") {

    override fun downloadTo(
        outputFile: File,
        interceptor: DownloadProgressInterceptor?
    ) {
            if (interceptor?.shouldStop() == true) return
            val video = Globals.youTubeDownloader.getVideo(videoId)
            val format = video.getBestFormat()!!

            video.apply {
                if (details().isLive) throw YoutubeException.LiveVideoException("Can not download live stream")

                val connection = URL(format.url()).openConnection()
                val totalSize = connection.contentLengthLong

                val inputStream = BufferedInputStream(connection.getInputStream())
                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(4096)
                var tmpCount = 0
                var readCount = 0L

                if (interceptor?.shouldStop() == true) return
                interceptor?.onProgress(0, totalSize)

                while (inputStream.read(buffer, 0, 4096).also { tmpCount = it } != -1) {
                    readCount += tmpCount
                    outputStream.write(buffer, 0, tmpCount)

                    if (interceptor?.shouldStop() == true) return
                    interceptor?.onProgress(readCount, totalSize)
                }

                outputStream.close()
                inputStream.close()
            }
    }

    override fun loadInfo(): Info {
        val video = Globals.youTubeDownloader.getVideo(videoId)
        return Info(
            format = video.getBestFormat()?.extension()?.value()?.let { if (it == "mp4") "mp3" else it } ?: "mp3",
            title = video.details().title(),
            tags = video.details().keywords()
        )
    }

    override fun toString(): String = "(YouTube) $videoId"

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