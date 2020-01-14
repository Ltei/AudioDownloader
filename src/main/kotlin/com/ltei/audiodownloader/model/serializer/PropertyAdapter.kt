package com.ltei.audiodownloader.model.serializer

import com.google.gson.*
import javafx.beans.property.*
import java.lang.reflect.Type

class ObjectPropertyAdapter<T>(
    private val typeOfT: Type
) : JsonSerializer<ObjectProperty<T>>, JsonDeserializer<ObjectProperty<T>> {
    override fun serialize(src: ObjectProperty<T>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src.value)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ObjectProperty<T> =
        SimpleObjectProperty(context.deserialize(json, this.typeOfT))

    companion object {
        inline fun <reified T> create() = ObjectPropertyAdapter<T>(T::class.java)
    }
}

class StringPropertyAdapter : JsonSerializer<StringProperty>, JsonDeserializer<StringProperty> {
    override fun serialize(src: StringProperty, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src.value)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): StringProperty =
        SimpleStringProperty(context.deserialize(json, String::class.java))
}

class BooleanPropertyAdapter : JsonSerializer<BooleanProperty>, JsonDeserializer<BooleanProperty> {
    override fun serialize(src: BooleanProperty, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src.value)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BooleanProperty =
        SimpleBooleanProperty(context.deserialize(json, Boolean::class.java))
}

class IntPropertyAdapter : JsonSerializer<IntegerProperty>, JsonDeserializer<IntegerProperty> {
    override fun serialize(src: IntegerProperty, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src.value)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): IntegerProperty =
        SimpleIntegerProperty(context.deserialize(json, Int::class.java))
}


class FloatPropertyAdapter : JsonSerializer<FloatProperty>, JsonDeserializer<FloatProperty> {
    override fun serialize(src: FloatProperty, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src.value)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FloatProperty =
        SimpleFloatProperty(context.deserialize(json, Float::class.java))
}


class DoublePropertyAdapter : JsonSerializer<DoubleProperty>, JsonDeserializer<DoubleProperty> {
    override fun serialize(src: DoubleProperty, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src.value)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DoubleProperty =
        SimpleDoubleProperty(context.deserialize(json, Double::class.java))
}

