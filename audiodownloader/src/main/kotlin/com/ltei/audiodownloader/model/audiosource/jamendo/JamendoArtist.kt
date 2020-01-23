package com.ltei.audiodownloader.model.audiosource.jamendo

import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import com.ltei.audiodownloader.web.jamendo.JamendoClient

data class JamendoArtist(
    val artistId: String
) : MultiAudioSourceUrl {
    override val label: String = "Jamendo channel ($artistId)"
    override val url: String = "https://www.jamendo.com/artist/$artistId"

    override fun getAudios(): List<AudioSourceUrl> {
        val response = JamendoClient.getTrack(artistId = artistId).execute()
        return response.body()!!.results.map { JamendoTrack(it.id) }
    }
}