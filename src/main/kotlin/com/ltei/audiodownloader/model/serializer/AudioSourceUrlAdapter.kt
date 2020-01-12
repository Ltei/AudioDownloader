package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.RawAudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.SoundCloudAudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.YouTubeAudioSourceUrl
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
            is RawAudioSourceUrl -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_RAW)
                addProperty(PROP_RAW_URL, src.rawUrl)
                addProperty(PROP_FORMAT, src.format)
            }
            is YouTubeAudioSourceUrl -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_YOUTUBE)
                addProperty(PROP_VIDEO_ID, src.videoId)
            }
            is SoundCloudAudioSourceUrl -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_SOUNDCLOUD)
                addProperty(PROP_RAW_URL, src.rawUrl)
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
                RawAudioSourceUrl(rawUrl, format)
            }
            TYPE_YOUTUBE -> {
                val videoId = jsonObj[PROP_VIDEO_ID].asString
                YouTubeAudioSourceUrl(videoId)
            }
            TYPE_SOUNDCLOUD -> {
                val rawUrl = jsonObj[PROP_RAW_URL].asString
                SoundCloudAudioSourceUrl(rawUrl)
            }
            else -> throw IllegalStateException()
        }
    }
}