package com.ltei.audiodownloader.model.audiourl

import com.ltei.audiodownloader.model.AudioMetadata

class RawAudioUrl(
    override val url: String,
    override val format: String = AudioSourceUtils.getRawUrlFormat(url) ?: "mp3"
) : MultiAudioSourceUrl, AudioSourceUrl, DownloadableAudioUrl {
    override val label get() = "Raw url"
    override fun getAudios() = listOf(this)
    override fun getDownloadableUrl() = DownloadableAudioUrl.Impl(url, format)
    override fun getMetadata() = AudioMetadata()
}