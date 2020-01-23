package com.ltei.audiodownloader.model.audiosource.soundcloud

import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl

class SoundCloudSet(
    val artistId: String,
    val playlistId: String
) : MultiAudioSourceUrl {
    override val url get() = "https://soundcloud.com/$artistId/sets/$playlistId"
    override val label get() = "SoundCloud set ($artistId - $playlistId)"
    override fun getAudios() = TODO()
}