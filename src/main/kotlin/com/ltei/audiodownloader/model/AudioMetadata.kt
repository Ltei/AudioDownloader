package com.ltei.audiodownloader.model

import com.ltei.audiodownloader.misc.debug.Logger
import com.ltei.audiodownloader.misc.util.FileUtils
import com.ltei.audiodownloader.misc.util.ListUtils
import com.ltei.audiodownloader.misc.util.hasNonBlankString
import com.ltei.audiodownloader.web.LastFmClient
import com.ltei.audiodownloader.web.SpotifyClient
import com.mpatric.mp3agic.Mp3File
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.io.File

data class AudioMetadata(
    var title: String? = null,
    var artists: List<String>? = null,
    var album: String? = null,
    var releaseDate: LocalDate? = null,
    var tags: List<String>? = null
) {
    fun autofill(audioFile: File? = null) {
        normalize()

        // Mp3 file metadata
        if (audioFile != null && audioFile.extension == FileUtils.Extension.MP3) {
            val mp3File = Mp3File(audioFile)
            if (mp3File.hasId3v1Tag()) {
                val tag = mp3File.id3v1Tag
                if (title.isNullOrBlank()) title = tag.title
                if (artists.isNullOrEmpty()) artists = listOf(tag.artist)
                if (album.isNullOrBlank()) album = tag.album
            } else if (mp3File.hasId3v2Tag()) {
                val tag = mp3File.id3v2Tag
                if (title.isNullOrBlank()) title = tag.title
                if (!artists.hasNonBlankString()) artists = listOf(tag.artist)
                if (album.isNullOrBlank()) album = tag.album
            }
        }

        // Spotify metadata
        title?.let { currentMetadataTitle ->
            val searchQuery = StringBuilder(currentMetadataTitle)
            artists?.let { artists ->
                for (artist in artists) searchQuery.append(' ').append(artist)
            }
            val track = SpotifyClient.searchTrack(searchQuery.toString()).firstOrNull()
            if (track != null) {
                if (!artists.hasNonBlankString()) artists = track.artists?.map { it.name }
                if (album.isNullOrBlank()) album = track.album?.name
            }
        }

        // LastFm metadata
        title?.let { currentMetadataTitle ->
            val dateTimeFormatter = DateTimeFormat.forPattern("dd MMM YYYY, HH:mm")
            val searchQuery = StringBuilder(currentMetadataTitle)
            artists?.let { artists ->
                for (artist in artists) searchQuery.append(' ').append(artist)
            }
            val track = LastFmClient.searchTrack(searchQuery.toString()).firstOrNull()
            if (track != null) {
                val info = LastFmClient.getTrackInfo(track.mbid)
                if (!artists.hasNonBlankString()) artists = listOf(info.artist.name)
                if (album.isNullOrBlank()) album = info.album.title
                try {
                    if (releaseDate == null) {
                        releaseDate = dateTimeFormatter.parseLocalDate(info.wiki.published)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                tags = ListUtils.concat(tags, info.topTags)
            }
        }

        normalize()
    }

    fun clear() {
        title = null
        artists = null
        album = null
        tags = null
    }

    fun normalize() {
        this.title?.let { title ->
            if (title.isBlank()) this.title = null
        }
        this.artists?.let { artists ->
            this.artists = if (artists.isEmpty() || artists.all { it.isBlank() }) null else {
                artists.distinctBy { it.toLowerCase() }.filter { it.isNotBlank() }
            }
        }
        this.album?.let { album ->
            if (album.isBlank()) this.album = null
        }
        this.tags?.let { tags ->
            this.tags = if (tags.isEmpty() || tags.all { it.isBlank() }) null else {
                tags.distinctBy { it.toLowerCase() }.filter { it.isNotBlank() }
            }
        }
    }

    companion object {
        private val logger = Logger(AudioMetadata::class.java)
    }
}