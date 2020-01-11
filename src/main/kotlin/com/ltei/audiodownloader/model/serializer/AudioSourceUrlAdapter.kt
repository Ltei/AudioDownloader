package com.ltei.audiodownloader.model.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.ltei.audiodownloader.model.AudioSourceUrl
import java.lang.reflect.Type

class AudioSourceUrlAdapter : JsonDeserializer<AudioSourceUrl> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AudioSourceUrl {
        val jsonObj = json.asJsonObject
        return when (jsonObj["sourceName"].asString) {
            "Raw" -> context.deserialize<AudioSourceUrl.Raw>(json, AudioSourceUrl.Raw::class.java)
            "YouTube" -> context.deserialize<AudioSourceUrl.YouTube>(json, AudioSourceUrl.YouTube::class.java)
            "SoundCloud" -> context.deserialize<AudioSourceUrl.SoundCloud>(json, AudioSourceUrl.SoundCloud::class.java)
            else -> throw IllegalStateException()
        }
    }
}