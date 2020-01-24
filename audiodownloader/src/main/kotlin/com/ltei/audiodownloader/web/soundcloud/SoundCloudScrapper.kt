package com.ltei.audiodownloader.web.soundcloud

import org.jsoup.Jsoup

object SoundCloudScrapper {

    private val audioTitleRegex = Regex("(.*) - (.*) by \\1 .*")

    data class AudioInfo(val title: String, val artist: String)
    fun getAudioInfo(audioUrl: String): AudioInfo? {
        val document = Jsoup.connect(audioUrl).timeout(15000) .get()
        val title = document.title()
        println(title)
        val result = audioTitleRegex.find(title)
        return if (result != null) {
            AudioInfo(
                result.groupValues[2],
                result.groupValues[1]
            )
        } else null
    }

    private val setTrackElementHrefRegex = Regex("/(.*)/(.*)")
    fun getSetAudioIds(artistId: String, setId: String): List<String> {
        val setUrl = "https://soundcloud.com/$artistId/sets/$setId"
        val document = Jsoup.connect(setUrl)
            .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
            .timeout(15000)
            .get()
        val trackListElement = document.getElementsByClass("tracklist").first()
        val numTracksElement = trackListElement.getElementsByAttributeValue("itemprop", "numTracks").first()
        val numTracks = numTracksElement.attributes()["content"].toInt()
        val result = trackListElement
            .getElementsByAttributeValue("itemprop", "track")
            .map { trackElement ->
                val href = trackElement
                    .children().first()
                    .children().first()
                    .attributes()["href"]
                val result = setTrackElementHrefRegex.find(href)!!
                result.groupValues[2]
            }
        if (numTracks != result.size) println("Couldn't fetch all tracks (${result.size}/$numTracks)")
        return result
    }

}