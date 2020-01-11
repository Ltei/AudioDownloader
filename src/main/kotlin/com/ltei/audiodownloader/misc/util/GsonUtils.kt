package com.ltei.audiodownloader.misc.util

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type


object GsonUtils {
    val prettyGson = GsonBuilder().setPrettyPrinting().create()

    fun addSmartCollectionSerializers(gsonBuilder: GsonBuilder) {
        gsonBuilder.registerTypeAdapter(List::class.java, SmartListSerializer())
        gsonBuilder.registerTypeAdapter(Collection::class.java, SmartCollectionSerializer())
    }
}

class SmartCollectionSerializer : JsonSerializer<Collection<*>?> {
    override fun serialize(src: Collection<*>?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement? {
        if (src == null || src.isEmpty()) // exclusion is made here
            return null
        val array = JsonArray()
        for (child in src) {
            val element = context.serialize(child)
            array.add(element)
        }
        return array
    }
}

class SmartListSerializer : JsonSerializer<List<*>?> {
    override fun serialize(src: List<*>?, typeOfSrc: Type?, context: JsonSerializationContext): JsonElement? {
        if (src == null || src.isEmpty()) // exclusion is made here
            return null
        val array = JsonArray()
        for (child in src) {
            val element = context.serialize(child)
            array.add(element)
        }
        return array
    }
}




// Gson methods

inline fun <reified T> Gson.fromJson(json: String): T = fromJson(json, T::class.java)!!
inline fun <reified T> Gson.fromJson(json: Reader): T = fromJson(json, T::class.java)!!
inline fun <reified T> Gson.fromJson(json: JsonElement): T = fromJson(json, T::class.java)!!

inline fun <reified T> Gson.fromJsonArray(json: String): List<T> = fromJson(json, GsonImpls.listType<T>())!!
inline fun <reified T> Gson.fromJsonArray(json: Reader): List<T> = fromJson(json, GsonImpls.listType<T>())!!
inline fun <reified T> Gson.fromJsonArray(json: JsonArray): List<T> = fromJson(json, GsonImpls.listType<T>())!!


// JsonObject methods

fun JsonObject.getOrNull(key: String): JsonElement? = if (has(key)) this[key] else null


// JsonElement methods

inline val JsonElement.isInt: Boolean get() = GsonImpls.tryCatchBool { asInt }
inline val JsonElement.isLong: Boolean get() = GsonImpls.tryCatchBool { asLong }
inline val JsonElement.isFloat: Boolean get() = GsonImpls.tryCatchBool { asFloat }
inline val JsonElement.isDouble: Boolean get() = GsonImpls.tryCatchBool { asDouble }
inline val JsonElement.isBoolean: Boolean get() = GsonImpls.tryCatchBool { asBoolean }
inline val JsonElement.isString: Boolean get() = GsonImpls.tryCatchBool { asString }

inline val JsonElement.asIntOrNull: Int? get() = GsonImpls.tryCatchNull { asInt }
inline val JsonElement.asLongOrNull: Long? get() = GsonImpls.tryCatchNull { asLong }
inline val JsonElement.asFloatOrNull: Float? get() = GsonImpls.tryCatchNull { asFloat }
inline val JsonElement.asDoubleOrNull: Double? get() = GsonImpls.tryCatchNull { asDouble }
inline val JsonElement.asBooleanOrNull: Boolean? get() = GsonImpls.tryCatchNull { asBoolean }
inline val JsonElement.asStringOrNull: String? get() = GsonImpls.tryCatchNull { asString }
inline val JsonElement.asJsonObjectOrNull: JsonObject? get() = GsonImpls.tryCatchNull { asJsonObject }
inline val JsonElement.asJsonArrayOrNull: JsonArray? get() = GsonImpls.tryCatchNull { asJsonArray }


/**
 * Contains misc functions used in this impl file
 */
object GsonImpls {
    inline fun <T> tryCatchNull(block: () -> T): T? = try {
        block()
    } catch (t: Throwable) {
        null
    }

    inline fun tryCatchBool(block: () -> Unit) = try {
        block()
        true
    } catch (t: Throwable) {
        false
    }

    inline fun <reified T> listType(): Type = TypeToken.getParameterized(List::class.java, T::class.java).type
}