package com.ltei.audiodownloader.model

import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.DownloadableAudioUrl
import java.io.File

data class AudioDownload(
    var source: AudioSourceUrl,
    val downloadedUrl: DownloadableAudioUrl,
    var outputFile: File,
    var state: State
) {

    sealed class State(val name: String) {
        object Waiting: State("Waiting")
        object Starting: State("Starting")
        data class InProgress(var progress: Long, var total: Long): State("InProgress") {
            override fun toString(): String = "$name($progress/$total, ${progress.toFloat()/total})"
        }
        object Finished: State("Finished")
        object Canceled: State("Canceled")

        override fun toString(): String = name
    }

}