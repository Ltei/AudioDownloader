package com.ltei.audiodownloader

import com.github.kiulian.downloader.YoutubeDownloader
import com.github.kiulian.downloader.cipher.CachedCipherFactory
import com.github.kiulian.downloader.cipher.CipherFactory
import com.github.kiulian.downloader.extractor.DefaultExtractor
import com.github.kiulian.downloader.extractor.Extractor
import com.github.kiulian.downloader.parser.DefaultParser
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.ltei.audiodownloader.model.AudioDownload
import com.ltei.audiodownloader.model.DownloadOutputMode
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.serializer.*
import javafx.beans.property.*
import java.io.File

object Globals {
    val youTubeDownloader = run {
        val extractor = DefaultExtractor()
        val cipherFactory = CachedCipherFactory(extractor)
        val parser = DefaultParser(extractor, cipherFactory)
        YoutubeDownloader()
    }

    val persistenceGson = GsonBuilder()
        .registerTypeAdapter(File::class.java, FileAdapter())
        .registerTypeAdapter(AudioDownload.State::class.java, AudioDownloadStateAdapter())
        .registerTypeAdapter(AudioSourceUrl::class.java, AudioSourceUrlAdapter())
        .registerTypeAdapter(DownloadOutputMode::class.java, DownloadOutputModeAdapter())

        .registerTypeAdapter(object : TypeToken<ObjectProperty<File>>() {}.type, ObjectPropertyAdapter.create<File>())
        .registerTypeAdapter(object : TypeToken<ObjectProperty<DownloadOutputMode>>() {}.type, ObjectPropertyAdapter.create<DownloadOutputMode>())
        .registerTypeAdapter(StringProperty::class.java, StringPropertyAdapter())
        .registerTypeAdapter(BooleanProperty::class.java, BooleanPropertyAdapter())
        .registerTypeAdapter(IntegerProperty::class.java, IntPropertyAdapter())
        .registerTypeAdapter(FloatProperty::class.java, FloatPropertyAdapter())
        .registerTypeAdapter(DoubleProperty::class.java, DoublePropertyAdapter())

        .setPrettyPrinting()
        .create()
}