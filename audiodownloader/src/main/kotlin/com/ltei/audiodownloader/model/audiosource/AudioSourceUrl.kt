package com.ltei.audiodownloader.model.audiosource

import com.ltei.audiodownloader.misc.thenCombineToPair
import com.ltei.audiodownloader.model.AudioMetadata
import java.util.concurrent.CompletableFuture

interface AudioSourceUrl {
    val label: String
    val url: String
    fun getDownloadableUrl(): CompletableFuture<DownloadableAudioUrl>
    fun getMetadata(): CompletableFuture<AudioMetadata>
    fun getDownloadableUrlAndMetadata(): CompletableFuture<Pair<DownloadableAudioUrl, AudioMetadata>> = getDownloadableUrl().thenCombineToPair(getMetadata())
}