package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import java.io.File

interface DownloadableAudioUrl {
    val url: String
    val format: String

    fun downloadTo(file: File, interceptor: DownloadProgressInterceptor? = null) =
        AudioSourceUtils.downloadRawUrlTo(url, file, interceptor)

    data class Impl(
        override val url: String,
        override val format: String
    ) : DownloadableAudioUrl
}