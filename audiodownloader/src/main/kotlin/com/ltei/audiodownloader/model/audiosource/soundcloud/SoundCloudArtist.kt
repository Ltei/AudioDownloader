package com.ltei.audiodownloader.model.audiosource.soundcloud

import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl

class SoundCloudArtist(
    val artistId: String
) : MultiAudioSourceUrl {
    override val url get() = "https://soundcloud.com/$artistId"
    override val label get() = "SoundCloud artist ($artistId)"
    override fun getAudios() = TODO()
}