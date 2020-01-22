package com.ltei.audiodownloader.model.audiourl

import com.ltei.audiodownloader.model.AudioMetadata

data class JamendoTrack(
    val trackId: String
): MultiAudioSourceUrl, AudioSourceUrl {
    override val label: String = "Jamendo track"
    override val url: String = "https://www.jamendo.com/track/$trackId"
    override fun getAudios() = listOf(this)

    override fun getDownloadableUrl(): DownloadableAudioUrl {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMetadata(): AudioMetadata {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}