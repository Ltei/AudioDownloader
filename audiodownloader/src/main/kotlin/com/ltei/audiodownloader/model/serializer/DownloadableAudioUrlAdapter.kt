package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import com.ltei.audiodownloader.model.audiourl.DownloadableAudioUrl
import java.lang.reflect.Type

class DownloadableAudioUrlAdapter : JsonSerializer<DownloadableAudioUrl>, JsonDeserializer<DownloadableAudioUrl> {
    override fun serialize(src: DownloadableAudioUrl, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonObject().apply {
            addProperty("url", src.url)
            addProperty("format", src.format)
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DownloadableAudioUrl {
        val jsonObj = json.asJsonObject
        return DownloadableAudioUrl.Impl(
            url = jsonObj["url"].asString,
            format = jsonObj["format"].asString
        )
    }
}