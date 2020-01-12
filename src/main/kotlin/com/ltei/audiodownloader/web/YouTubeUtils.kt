//package com.ltei.audiodownloader.web
//
//import com.github.kiulian.downloader.YoutubeDownloader
//import com.github.kiulian.downloader.YoutubeException.LiveVideoException
//import com.github.kiulian.downloader.model.quality.AudioQuality
//import com.ltei.audiodownloader.model.DownloadProgressInterceptor
//import java.io.BufferedInputStream
//import java.io.File
//import java.io.FileOutputStream
//import java.net.URL
//
//object YouTubeUtils {
//
////    private val client = YouTube.Builder(NetHttpTransport(), JacksonFactory(), HttpRequestInitializer {
////        it.headers.set("X-Android-Package", context.packageName)
////        it.headers.set("X-Android-Cert", sha1)
////    })/*.setApplicationName(applicationName)*/.build()
//
////    fun getVideoSnippet(videoId: String): Video? {
////        val response = client.Videos().list("snippet").setId(videoId).execute()
////        return response?.items?.firstOrNull()
////    }
//
//    fun getVideoInfo(videoId: String): VideoInfo {
//        val video = scrapperClient.getVideo(videoId)
//        return VideoInfo(
//            title = video.details().title(),
//            artist = null
//        )
//    }
//
//    fun downloadVideoAudioTo(videoId: String, outputFileName: String, interceptor: DownloadProgressInterceptor) {
//        if (interceptor.shouldStop()) return
//
//        val video = scrapperClient.getVideo(videoId)
//        val format = video.audioFormats().maxBy {
//            when (it.audioQuality()!!) {
//                AudioQuality.unknown -> 0
//                AudioQuality.high -> 3
//                AudioQuality.medium -> 2
//                AudioQuality.low -> 1
//                AudioQuality.noAudio -> -1
//            }
//        }!!
//
//        video.apply {
//            if (details().isLive) throw LiveVideoException("Can not download live stream")
//
//            val outputFile = File("$outputFileName.${format.extension().value()}")
//
//            val connection = URL(format.url()).openConnection()
//            val totalSize = connection.contentLengthLong
//
//            val inputStream = BufferedInputStream(connection.getInputStream())
//            val outputStream = FileOutputStream(outputFile)
//            val buffer = ByteArray(4096)
//            var tmpCount = 0
//            var readCount = 0L
//
//            if (interceptor.shouldStop()) return
//            interceptor.onProgress(0, totalSize)
//            while (inputStream.read(buffer, 0, 4096).also { tmpCount = it } != -1) {
//                readCount += tmpCount
//                if (interceptor.shouldStop()) return
//                outputStream.write(buffer, 0, tmpCount)
//                interceptor.onProgress(readCount, totalSize)
//            }
//            outputStream.close()
//            inputStream.close()
//        }
//    }
//
//    data class VideoInfo(
//        val audioFormat: String,
//        val title: String? = null,
//        val artist: String? = null
//    )
//
//}