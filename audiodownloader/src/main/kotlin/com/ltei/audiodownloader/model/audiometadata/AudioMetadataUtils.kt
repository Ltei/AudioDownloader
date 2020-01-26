package com.ltei.audiodownloader.model.audiometadata

import com.ltei.audiodownloader.web.LastFmClient
import com.ltei.ljuutils.datamanager.DefaultListPropertyManager
import com.ltei.ljuutils.datamanager.DefaultIdentifiablePropertyManager
import com.mpatric.mp3agic.Mp3File
import com.wrapper.spotify.model_objects.specification.Track

object AudioMetadataUtils {
    fun createSearchQuery(metadata: AudioMetadata): String? {
        if (metadata.title == null) return null
        val searchQuery = StringBuilder(metadata.title)
        metadata.artists?.let { artists ->
            for (artist in artists) searchQuery.append(' ').append(artist)
        }
        return searchQuery.toString()
    }

    fun fromMp3File(mp3File: Mp3File): AudioMetadata {
        return AudioMetadata().apply {
            if (mp3File.hasId3v1Tag()) {
                val tag = mp3File.id3v1Tag
                title = tag.title
                artists = listOf(AudioArtist(name = tag.artist))
                album = AudioAlbum(title = tag.album)
            } else if (mp3File.hasId3v2Tag()) {
                val tag = mp3File.id3v2Tag
                title = tag.title
                artists = listOf(AudioArtist(name = tag.artist))
                album = AudioAlbum(title = tag.album)
            }
            normalize()
        }
    }

    fun fromSpotifyTrack(track: Track): AudioMetadata {
        return AudioMetadata(
            title = track.name,
            artists = track.artists?.map {
                AudioArtist(name = it?.name)
            },
            album = AudioAlbum(title = track.album?.name)
        )
    }

    fun fromLastFmTrack(track: LastFmClient.TrackInfo): AudioMetadata {
        return AudioMetadata(
            title = track.name,
            artists = listOf(
                AudioArtist(name = track.artist.name)
            ),
            album = AudioAlbum(title = track.album.title)
        )
    }

    class NameListPropertyManager : DefaultListPropertyManager<String>(NamePropertyManager())
    class NamePropertyManager : DefaultIdentifiablePropertyManager<String>() {
        override fun isEmptyImpl(data: String) = data.isBlank()
        override fun areSameDataImpl(a: String, b: String): Boolean = a.equals(b, ignoreCase = true)
    }

    class UrlListPropertyManager : DefaultListPropertyManager<String>(UrlPropertyManager())
    class UrlPropertyManager : DefaultIdentifiablePropertyManager<String>() {
        override fun isEmptyImpl(data: String) = data.isBlank()
        override fun areSameDataImpl(a: String, b: String): Boolean = a.equals(b, ignoreCase = false)
    }
}