package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import com.ltei.audiodownloader.model.AudioDownload
import java.lang.reflect.Type

class AudioDownloadStateAdapter : JsonDeserializer<AudioDownload.State> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AudioDownload.State {
        val jsonObj = json.asJsonObject
        return when (jsonObj["name"].asString) {
            AudioDownload.State.Waiting.name -> AudioDownload.State.Waiting
            AudioDownload.State.Starting.name -> AudioDownload.State.Starting
            "InProgress" -> {
                context.deserialize<AudioDownload.State.InProgress>(json, AudioDownload.State.InProgress::class.java)
            }
            AudioDownload.State.Finished.name -> AudioDownload.State.Finished
            AudioDownload.State.Canceled.name -> AudioDownload.State.Canceled
            else -> throw IllegalStateException()
        }
    }
}