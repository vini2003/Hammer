package com.github.vini2003.blade.common.data

import com.google.gson.JsonElement
import java.util.function.Function

class Style(private var properties: MutableMap<String, JsonElement>) {
	companion object {
		val EMPTY: Style = Style(mutableMapOf())

		private var deserializers: MutableMap<Class<*>, Function<JsonElement, *>> = mutableMapOf()

		init {
			registerDeserializer(Color::class.java, Function<JsonElement, Color> {
				Color.of(it.asString)
			})
			registerDeserializer(Byte::class.java, Function<JsonElement, Byte> {
				it.asByte
			})
			registerDeserializer(Short::class.java, Function<JsonElement, Short> {
				it.asShort
			})
			registerDeserializer(Int::class.java, Function<JsonElement, Int> {
				it.asInt
			})
			registerDeserializer(Long::class.java, Function<JsonElement, Long> {
				it.asLong
			})
			registerDeserializer(Float::class.java, Function<JsonElement, Float> {
				it.asFloat
			})
			registerDeserializer(Double::class.java, Function<JsonElement, Double> {
				it.asDouble
			})
			registerDeserializer(String::class.java, Function<JsonElement, String> {
				it.asString
			})
		}

		fun <T> registerDeserializer(clazz: Class<T>, deserializer: Function<JsonElement, T>) {
			deserializers[clazz] = deserializer
		}
	}

	fun <T> get(property: String, clazz: Class<T>): T? {
		return properties[property]?.let { deserializers[clazz]?.apply(it) } as T
	}

	fun asColor(property: String): Color {
		return get(property, Color::class.java) ?: Color.default()
	}

	fun asByte(property: String): Byte {
		return get(property, Byte::class.java) ?: 0.toByte()
	}

	fun asShort(property: String): Short {
		return get(property, Short::class.java) ?: 0.toShort()
	}

	fun asInt(property: String): Int {
		return get(property, Int::class.java) ?: 0
	}

	fun asLong(property: String): Long {
		return get(property, Long::class.java) ?: 0.toLong()
	}

	fun asFloat(property: String): Float {
		return get(property, Float::class.java) ?: 0.toFloat()
	}

	fun asDouble(property: String): Double {
		return get(property, Double::class.java) ?: 0.toDouble()
	}

	fun asString(property: String): String {
		return get(property, String::class.java) ?: ""
	}
}