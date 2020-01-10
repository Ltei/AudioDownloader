package com.ltei.audiodownloader.web

import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video

object YouTubeUtils {

    private val client = YouTube.Builder(NetHttpTransport(), JacksonFactory(), HttpRequestInitializer {
//        it.headers.set("X-Android-Package", context.packageName)
//        it.headers.set("X-Android-Cert", sha1)
    })/*.setApplicationName(applicationName)*/.build()

    fun getVideoSnippet(videoId: String): Video? {
        val response = client.Videos().list("snippet").setId(videoId).execute()
        return response?.items?.firstOrNull()
    }

}