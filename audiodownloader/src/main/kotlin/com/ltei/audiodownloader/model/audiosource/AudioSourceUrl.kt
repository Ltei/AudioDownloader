package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.model.AudioMetadata

interface AudioSourceUrl {
    val label: String
    val url: String
    fun getDownloadableUrl(): DownloadableAudioUrl
    fun getMetadata(): AudioMetadata
    fun getDownloadableUrlAndMetadata(): Pair<DownloadableAudioUrl, AudioMetadata> = Pair(getDownloadableUrl(), getMetadata())
}