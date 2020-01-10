package com.ltei.audiodownloader.model

interface DownloadProgressListener {
    fun onProgress(progress: Long, total: Long)
}