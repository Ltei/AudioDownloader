package com.ltei.audiodownloader.web

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
            AudioInfo(result.groupValues[2], result.groupValues[1])
        } else null
    }

}