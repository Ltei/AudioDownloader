package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.RawAudioUrl
import com.ltei.audiodownloader.model.audiosource.YouTubeVideoUrl
import com.ltei.audiodownloader.model.audiosource.SoundCloudTrackUrl
import java.lang.reflect.Type

class AudioSourceUrlAdapter : JsonSerializer<AudioSourceUrl>, JsonDeserializer<AudioSourceUrl> {
    companion object {
        private const val PROP_TYPE = "type"
        private const val PROP_RAW_URL = "rawUrl"
        private const val PROP_FORMAT = "format"
        private const val PROP_VIDEO_ID = "videoId"

        private const val TYPE_RAW = 0
        private const val TYPE_YOUTUBE = 1
        private const val TYPE_SOUNDCLOUD = 2
    }
    override fun serialize(
        src: AudioSourceUrl,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            is RawAudioUrl -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_RAW)
                addProperty(PROP_RAW_URL, src.url)
                addProperty(PROP_FORMAT, src.format)
            }
            is YouTubeVideoUrl -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_YOUTUBE)
                addProperty(PROP_VIDEO_ID, src.videoId)
            }
            is SoundCloudTrackUrl -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_SOUNDCLOUD)
                addProperty(PROP_RAW_URL, src.url)
            }
            else -> throw IllegalStateException()
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AudioSourceUrl {
        val jsonObj = json.asJsonObject
        return when (jsonObj[PROP_TYPE].asInt) {
            TYPE_RAW -> {
                val rawUrl = jsonObj[PROP_RAW_URL].asString
                val format = jsonObj[PROP_FORMAT].asString
                RawAudioUrl(rawUrl, format)
            }
            TYPE_YOUTUBE -> {
                val videoId = jsonObj[PROP_VIDEO_ID].asString
                YouTubeVideoUrl(videoId)
            }
            TYPE_SOUNDCLOUD -> {
                val rawUrl = jsonObj[PROP_RAW_URL].asString
                SoundCloudTrackUrl(rawUrl)
            }
            else -> throw IllegalStateException()
        }
    }
}