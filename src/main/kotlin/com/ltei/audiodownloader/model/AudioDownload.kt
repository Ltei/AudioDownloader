package com.ltei.audiodownloader.model

import java.io.File

data class AudioDownload(
    var source: AudioSourceUrl,
    var outputFile: File,
    var state: State = State.Waiting
) {

    sealed class State {
        object Waiting: State()
        object Starting: State()
        class InProgress(var progress: Long, var total: Long): State()
        object Finished: State()
    }

}