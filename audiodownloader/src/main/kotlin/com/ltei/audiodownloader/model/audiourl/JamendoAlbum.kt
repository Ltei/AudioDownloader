package com.ltei.audiodownloader.model.audiourl

data class JamendoAlbum(
    val albumId: String
) : MultiAudioSourceUrl {
    override val label: String = "Jamendo channel"
    override val url: String = "https://www.jamendo.com/channel/$albumId"

    override fun getAudios(): List<AudioSourceUrl> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}