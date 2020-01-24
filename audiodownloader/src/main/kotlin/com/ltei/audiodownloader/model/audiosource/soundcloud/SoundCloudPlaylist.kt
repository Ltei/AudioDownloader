package com.ltei.audiodownloader.model.audiosource.soundcloud

import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.MultiAudioSourceUrl
import com.ltei.audiodownloader.web.soundcloud.SoundCloudClient

class SoundCloudPlaylist(
    val artistId: String,
    val playlistId: String
) : MultiAudioSourceUrl {
    override val url get() = "https://soundcloud.com/$artistId/sets/$playlistId"
    override val label get() = "SoundCloud set ($artistId - $playlistId)"
    override fun getAudios(): List<AudioSourceUrl> {
        val playlist = SoundCloudClient.resolveResource(url).execute().body() as SoundCloudClient.Playlist
        return playlist.tracks.map {
            SoundCloudTrack(it.user.permalink, it.permalink)
        }
    }
}