package com.ltei.audiodownloader.model.audiosource.jamendo

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.DownloadableAudioUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import com.ltei.audiodownloader.web.jamendo.JamendoClient

data class JamendoTrack(
    val trackId: String
): MultiAudioSourceUrl,
    AudioSourceUrl {
    override val label: String = "Jamendo track ($trackId)"
    override val url: String = "https://www.jamendo.com/track/$trackId"
    override fun getAudios() = listOf(this)

    override fun getDownloadableUrl(): DownloadableAudioUrl {
        val url = getTrackCall().execute().body()!!.results.first().audioDownload
        return DownloadableAudioUrl.Impl(url, "mp3")
    }

    override fun getMetadata(): AudioMetadata {
        val track = getTrackCall().execute().body()!!.results.first()
        return AudioMetadata(
            title = track.name,
            artists = listOf(track.artistName),
            album = track.albumName
            // releaseDate = track.releaseDate TODO Parse date
        )
    }

    fun getTrackCall() = JamendoClient.getTrack(id = trackId)
}