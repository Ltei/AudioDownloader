package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import com.ltei.audiodownloader.model.audiosource.AudioSourceUrl
import com.ltei.audiodownloader.model.audiosource.RawAudioUrl
import com.ltei.audiodownloader.model.audiosource.jamendo.JamendoTrack
import com.ltei.audiodownloader.model.audiosource.youtube.YouTubeVideo
import com.ltei.audiodownloader.model.audiosource.soundcloud.SoundCloudTrack
import java.lang.reflect.Type

class AudioSourceUrlAdapter : JsonSerializer<AudioSourceUrl>, JsonDeserializer<AudioSourceUrl> {
    companion object {
        private const val PROP_TYPE = "type"
        private const val PROP_RAW_URL = "rawUrl"
        private const val PROP_FORMAT = "format"
        private const val PROP_VIDEO_ID = "videoId"
        private const val PROP_TRACK_ID = "trackId"
        private const val PROP_ARTIST_ID = "artistId"

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
            is RawAudioUrl -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_RAW)
                addProperty(PROP_RAW_URL, src.url)
                addProperty(PROP_FORMAT, src.format)
            }
            is YouTubeVideo -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_YOUTUBE_VIDEO)
                addProperty(PROP_VIDEO_ID, src.videoId)
            }
            is SoundCloudTrack -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_SOUNDCLOUD_TRACK)
                addProperty(PROP_ARTIST_ID, src.artistId)
                addProperty(PROP_TRACK_ID, src.trackId)
            }
            is JamendoTrack -> JsonObject().apply {
                addProperty(PROP_TYPE, TYPE_JAMENDO_TRACK)
                addProperty(PROP_TRACK_ID, src.trackId)
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
            TYPE_RAW -> {
                val rawUrl = jsonObj[PROP_RAW_URL].asString
                val format = jsonObj[PROP_FORMAT].asString
                RawAudioUrl(rawUrl, format)
            }
            TYPE_YOUTUBE_VIDEO -> {
                val videoId = jsonObj[PROP_VIDEO_ID].asString
                YouTubeVideo(videoId)
            }
            TYPE_SOUNDCLOUD_TRACK -> {
                val artistId = jsonObj[PROP_ARTIST_ID].asString
                val trackId = jsonObj[PROP_TRACK_ID].asString
                SoundCloudTrack(artistId, trackId)
            }
            TYPE_JAMENDO_TRACK -> {
                val trackId = jsonObj[PROP_TRACK_ID].asString
                JamendoTrack(trackId)
            }
            else -> throw IllegalStateException()
        }
    }
}