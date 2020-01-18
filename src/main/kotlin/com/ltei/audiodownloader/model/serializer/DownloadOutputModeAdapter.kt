package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import com.ltei.audiodownloader.model.DownloadOutputMode
import java.lang.reflect.Type

class DownloadOutputModeAdapter : JsonSerializer<DownloadOutputMode>, JsonDeserializer<DownloadOutputMode> {
    override fun serialize(src: DownloadOutputMode, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.name)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext) =
        DownloadOutputMode.valueOf(json.asString)
}