package com.ltei.audiodownloader.model.audiourl

data class YouTubeChannelUrl(
    val channelId: String
): MultiAudioSourceUrl {
    override val label: String = "YouTube channel"
    override val url: String = "https://www.youtube.com/channel/$channelId"

    override fun getAudios(): List<AudioSourceUrl> {
        TODO()
    }
}