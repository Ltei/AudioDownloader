package com.ltei.audiodownloader.model

import java.io.File

data class DownloadedAudio(
    val source: AudioSourceUrl,
    val outputFile: File
)