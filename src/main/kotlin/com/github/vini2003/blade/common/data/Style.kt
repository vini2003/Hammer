package com.github.vini2003.blade.common.data

import blue.endless.jankson.JsonElement
import blue.endless.jankson.JsonPrimitive
import java.util.function.Function
import kotlin.reflect.KClass

class Style(private var properties: MutableMap<String, JsonElement>) {
    companion object {
        val EMPTY: Style = Style(mutableMapOf())
    }

    private var deserializers: MutableMap<Class<*>, Function<JsonElement, *>> = mutableMapOf()

    init {
        registerDeserializer(Color::class.java, Function<JsonElement, Color> {
            Color.of(JsonPrimitive(it).asInt(Int.MAX_VALUE))
        })
        registerDeserializer(Byte::class.java, Function<JsonElement, Byte> {
            JsonPrimitive(it).asByte(Byte.MAX_VALUE)
        })
        registerDeserializer(Short::class.java, Function<JsonElement, Short> {
            JsonPrimitive(it).asShort(Short.MAX_VALUE)
        })
        registerDeserializer(Int::class.java, Function<JsonElement, Int> {
            JsonPrimitive(it).asInt(Int.MAX_VALUE)
        })
        registerDeserializer(Long::class.java, Function<JsonElement, Long> {
            JsonPrimitive(it).asLong(Long.MAX_VALUE)
        })
        registerDeserializer(Float::class.java, Function<JsonElement, Float> {
            JsonPrimitive(it).asFloat(Float.MAX_VALUE)
        })
        registerDeserializer(Double::class.java, Function<JsonElement, Double> {
            JsonPrimitive(it).asDouble(Double.MAX_VALUE)
        })
        registerDeserializer(String::class.java, Function<JsonElement, String> {
            JsonPrimitive(it).asString()
        })
    }

    fun <T> registerDeserializer(clazz: Class<T>, deserializer: Function<JsonElement, T>) {
        deserializers[clazz] = deserializer
    }

    fun <T> get(property: String, clazz: Class<T>): T? {
        return properties[property]?.let { deserializers[clazz]?.apply(it) } as T
    }

    fun asColor(property: String): Color {
        return get(property, Color::class.java) as Color
    }

    fun asByte(property: String): Byte {
        return get(property, Byte::class.java) as Byte
    }

    fun asShort(property: String): Short {
        return get(property, Short::class.java) as Short
    }

    fun asInt(property: String): Int {
        return get(property, Int::class.java) as Int
    }

    fun asLong(property: String): Long {
        return get(property, Long::class.java) as Long
    }

    fun asFloat(property: String): Float {
        return get(property, Float::class.java) as Float
    }

    fun asDouble(property: String): Double {
        return get(property, Double::class.java) as Double
    }

    fun asString(property: String): String {
        return get(property, String::class.java) as String
    }
}