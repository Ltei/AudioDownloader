package com.ltei.audiodownloader.model.audiosource.jamendo

import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import com.ltei.audiodownloader.web.jamendo.JamendoClient

data class JamendoAlbum(
    val albumId: String
) : MultiAudioSourceUrl {
    override val label: String = "Jamendo channel"
    override val url: String = "https://www.jamendo.com/channel/$albumId"

    override fun getAudios(): List<AudioSourceUrl> {
        val response = JamendoClient.getTrack(albumId = albumId).execute()
        return response.body()!!.results.map { JamendoTrack(it.id) }
    }
}