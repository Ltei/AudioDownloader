package com.ltei.audiodownloader.model.audiourl

import com.ltei.audiodownloader.model.audiometadata.AudioMetadata
import java.net.URL
import java.util.concurrent.CompletableFuture

class RawAudioUrl(
    override val url: String,
    override val format: String = AudioSourceUtils.getRawUrlFormat(url) ?: "mp3"
) : AudioSourceUrlProvider, AudioSourceUrl, DownloadableAudioUrl {
    override val label get() = LABEL
    override fun getAudioSourceUrls(): CompletableFuture<List<AudioSourceUrl>> = CompletableFuture.completedFuture(listOf<AudioSourceUrl>(this))
    override fun getDownloadableUrl(): CompletableFuture<DownloadableAudioUrl> = CompletableFuture.completedFuture(this)
    override fun getMetadata(): CompletableFuture<AudioMetadata> = CompletableFuture.completedFuture(
        AudioMetadata()
    )

    companion object {
        const val LABEL = "Raw url"
        fun parse(url: URL) = RawAudioUrl(url.toString())
        fun parseLabel(url: URL) = LABEL
    }
}