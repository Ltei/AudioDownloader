package com.ltei.audiodownloader.model

interface DownloadProgressInterceptor {
    fun onProgress(progress: Long, total: Long)
    fun shouldStop(): Boolean = false
}