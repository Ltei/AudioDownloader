package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.model.AudioMetadata
import java.net.URL
import java.util.concurrent.CompletableFuture

class RawAudioUrl(
    override val url: String,
    override val format: String = AudioSourceUtils.getRawUrlFormat(url) ?: "mp3"
) : MultiAudioSourceUrl, AudioSourceUrl, DownloadableAudioUrl {
    override val label get() = "Raw url"
    override fun getAudios() = CompletableFuture.completedFuture(listOf<AudioSourceUrl>(this))
    override fun getDownloadableUrl(): CompletableFuture<DownloadableAudioUrl> = CompletableFuture.completedFuture(this)
    override fun getMetadata() = CompletableFuture.completedFuture(AudioMetadata())

    companion object {
        fun parse(url: URL) = RawAudioUrl(url.toString())
        fun parseLabel(url: URL) = "Raw url"
    }
}