package com.ltei.audiodownloader.model.audiometadata

import com.ltei.audiodownloader.web.LastFmClient
import com.ltei.audiodownloader.web.SpotifyClient
import com.ltei.ljuutils.datamanager.ManagedByClass
import com.ltei.ljuutils.datamanager.ManagedByCompanionProperty
import com.ltei.ljuutils.datamanager.ObjectManagerFactory
import org.joda.time.LocalDate

data class AudioMetadata(
    @ManagedByClass(AudioMetadataUtils.NamePropertyManager::class)
    var title: String? = null,

    @ManagedByClass(AudioMetadataUtils.UrlListPropertyManager::class)
    var images: List<String>? = null,

    @ManagedByCompanionProperty("artistsManager")
    var artists: List<AudioArtist>? = null,

    @ManagedByCompanionProperty("albumManager")
    var album: AudioAlbum? = null,

    var releaseDate: LocalDate? = null,

    @ManagedByClass(AudioMetadataUtils.NameListPropertyManager::class)
    var tags: List<String>? = null
) {

    fun clear() = manager.clear(this)
    fun isEmpty() = manager.isEmpty(this)
    fun normalize() = manager.normalize(this)
    fun updateWith(new: AudioMetadata?) = manager.updateWith(this, new)

    fun autofill() {
        val searchQuery = AudioMetadataUtils.createSearchQuery(this)

        if (searchQuery != null) {
            // Spotify metadata
            try {
                val spotifyClient = SpotifyClient.instance
                if (spotifyClient != null) {
                    val track = spotifyClient.searchTrack(searchQuery.toString()).firstOrNull()
                    if (track != null) {
                        val metadata = AudioMetadataUtils.fromSpotifyTrack(track)
                        updateWith(metadata)
                    }
                }
            } catch (e: Exception) {
                IllegalStateException("Error while trying to get info from Spotify", e).printStackTrace()
            }

            // LastFm metadata
            try {
                val track = LastFmClient.searchTrack(searchQuery.toString()).firstOrNull()
                if (track != null) {
                    val trackInfo = LastFmClient.getTrackInfo(track.mbid)
                    val metadata = AudioMetadataUtils.fromLastFmTrack(trackInfo)
                    updateWith(metadata)
                }
            } catch (e: Exception) {
                IllegalStateException("Error while trying to get info from LastFm", e).printStackTrace()
            }

            // Clean
            normalize()
        }
    }

    companion object {
        val manager = ObjectManagerFactory().create<AudioMetadata>()
        val artistsManager = AudioArtist.propertyListManager
        val albumManager = AudioAlbum.propertyManager
    }

}