package com.ltei.audiodownloader.web

import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.model_objects.specification.Track
import java.net.URI

object SpotifyClient {

    val spotify = SpotifyApi.builder()
        .setClientId("dcc361538cf44a2492d2884f05e7f40e")
        .setClientSecret("5498695c997849b49be56c2381639674")
        .setRedirectUri(URI.create("http://localhost:8888/callback"))
        .build()


    init {
        val credentialsResponse = spotify.clientCredentials().build().execute()
        spotify.accessToken = credentialsResponse.accessToken
    }

    fun searchTrack(query: String): List<Track> {
        val tracksResponse = spotify.searchTracks(query).build().execute()
        return tracksResponse.items.toList()
    }

}