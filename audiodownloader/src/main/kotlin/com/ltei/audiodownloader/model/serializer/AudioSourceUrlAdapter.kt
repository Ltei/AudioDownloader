package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import com.ltei.audiodownloader.model.audiourl.AudioSourceUrl
import com.ltei.audiodownloader.model.audiourl.RawAudioUrl
import com.ltei.audiodownloader.model.audiourl.jamendo.JamendoTrack
import com.ltei.audiodownloader.model.audiourl.youtube.YouTubeVideo
import com.ltei.audiodownloader.model.audiourl.soundcloud.SoundCloudTrack
import java.lang.reflect.Type

class AudioSourceUrlAdapter : JsonSerializer<AudioSourceUrl>, JsonDeserializer<AudioSourceUrl> {
    companion object {
        private const val PROP_TYPE = "type"

        private const val TYPE_RAW = "TYPE_RAW"
        private const val TYPE_YOUTUBE_VIDEO = "TYPE_YOUTUBE_VIDEO"
        private const val TYPE_SOUNDCLOUD_TRACK = "TYPE_SOUNDCLOUD_TRACK"
        private const val TYPE_JAMENDO_TRACK = "TYPE_JAMENDO_TRACK"
    }
    override fun serialize(
        src: AudioSourceUrl,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            is RawAudioUrl -> context.serialize(src, RawAudioUrl::class.java).let {
                it.asJsonObject.apply { addProperty(PROP_TYPE, TYPE_RAW) }
            }
            is YouTubeVideo -> context.serialize(src, YouTubeVideo::class.java).let {
                it.asJsonObject.apply { addProperty(PROP_TYPE, TYPE_YOUTUBE_VIDEO) }
            }
            is SoundCloudTrack -> context.serialize(src, SoundCloudTrack::class.java).let {
                it.asJsonObject.apply { addProperty(PROP_TYPE, TYPE_SOUNDCLOUD_TRACK) }
            }
            is JamendoTrack -> context.serialize(src, JamendoTrack::class.java).let {
                it.asJsonObject.apply { addProperty(PROP_TYPE, TYPE_JAMENDO_TRACK) }
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
        return when (jsonObj[PROP_TYPE].asString) {
            TYPE_RAW -> context.deserialize(json, RawAudioUrl::class.java)
            TYPE_YOUTUBE_VIDEO -> context.deserialize(json, YouTubeVideo::class.java)
            TYPE_SOUNDCLOUD_TRACK -> context.deserialize(json, SoundCloudTrack::class.java)
            TYPE_JAMENDO_TRACK -> context.deserialize(json, JamendoTrack::class.java)
            else -> throw IllegalStateException()
        }
    }
}