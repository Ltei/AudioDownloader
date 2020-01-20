package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import java.io.File

class RawAudioSourceUrl(
    rawUrl: String,
    val format: String = rawUrl.takeLastWhile { it != '.' }
) : AudioSourceUrl("Raw", rawUrl) {
    override fun downloadTo(outputFile: File, interceptor: DownloadProgressInterceptor?) {
        AudioSourceUtils.downloadRawUrlTo(rawUrl, outputFile, interceptor)
    }

    override fun loadInfo(): Info = Info(
        format = format,
        metadata = AudioMetadata()
    )

    override fun toString(): String = "(Raw url) $rawUrl"
}