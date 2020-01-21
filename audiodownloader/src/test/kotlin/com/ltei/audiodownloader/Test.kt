package com.ltei.audiodownloader

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ltei.audiodownloader.model.DownloadProgressInterceptor
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.web.jamendo.Format
import com.ltei.audiodownloader.web.jamendo.JamendoClient
import org.junit.Test
import java.io.File

class Test {

    @Test
    fun test() {
        val artists = JamendoClient.getArtist(nameSearch = "abi").execute().body()!!
        val tracks = JamendoClient.getTrack(artistId = artists.results.first().id.toInt()).execute().body()!!
        println(GsonBuilder().setPrettyPrinting().create().toJson(tracks))
        val url = AudioSourceUrl.parse(tracks.results.first().audioDownload)!!
        url.downloadTo(File("test.mp3"), interceptor = object : DownloadProgressInterceptor {
            override fun onProgress(progress: Long, total: Long) {
                println(progress)
            }
        })
    }

}