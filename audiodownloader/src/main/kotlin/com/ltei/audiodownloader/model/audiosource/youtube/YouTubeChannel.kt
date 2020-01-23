package com.ltei.audiodownloader.model.audiosource.youtube

import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl

data class YouTubeChannel(
    val channelId: String
): MultiAudioSourceUrl {
    override val label: String = "YouTube channel"
    override val url: String = "https://www.youtube.com/channel/$channelId"

    override fun getAudios(): List<AudioSourceUrl> {
        TODO()
    }
}