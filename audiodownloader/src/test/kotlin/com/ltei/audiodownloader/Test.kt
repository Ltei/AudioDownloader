package com.ltei.audiodownloader

import com.google.gson.GsonBuilder
import com.ltei.audiodownloader.web.jamendo.Boost
import com.ltei.audiodownloader.web.jamendo.JamendoClient
import com.ltei.audiodownloader.web.jamendo.Order
import com.ltei.ljuutils.utils.ListUtils
import org.junit.Test
import java.net.URL

class Test {

    @Test
    fun test() {
//        val artists = JamendoClient.getArtist(nameSearch = "abi").execute().body()!!
//        val tracks = JamendoClient.getTrack(artistId = artists.results.first().id.toInt()).execute().body()!!
//        println(GsonBuilder().setPrettyPrinting().create().toJson(tracks))
//        val url = AudioSourceUrl.parse(tracks.results.first().audioDownload)!!
//        url.downloadTo(File("test.mp3"), interceptor = object : DownloadProgressInterceptor {
//            override fun onProgress(progress: Long, total: Long) {
//                println(progress)
//            }
//        })

//        val tracks = JamendoClient.getTrack(
//            xArtist = "Kanye West",
//            order = Order.BuzzRate,
//            boost = Boost.BuzzRate
//        ).execute().body()!!
//        println(GsonBuilder().setPrettyPrinting().create().toJson(tracks))

        val url = URL("https://www.youtube.com/channel/UCahJ8PJViRpQppsYPlAAYKg")
        println(url.host)
        println(ListUtils.format(url.path.split("/")))
        println(url.query)
    }

}