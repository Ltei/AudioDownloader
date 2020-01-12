package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import java.io.File
import java.lang.reflect.Type

class FileAdapter : JsonSerializer<File>, JsonDeserializer<File> {
    override fun serialize(src: File, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        JsonPrimitive(src.absolutePath)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): File =
        File(json.asString)
}