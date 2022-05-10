package dev.vini2003.hammer.core.api.common.util.serializer.format

import dev.vini2003.hammer.core.api.common.util.serializer.module.HammerSerializersModule
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import net.minecraft.nbt.*

class Nbt(
	val context: SerializersModule = EmptySerializersModule
) : SerialFormat {
	override val serializersModule = context + HammerSerializersModule
	
	fun <T> serializeToNbt(serializer: SerializationStrategy<T>, value: T): NbtElement {
		lateinit var result: NbtElement
		
		val encoder = NbtEncoder(this) { element ->
			result = element
		}
		
		encoder.encodeSerializableValue(serializer, value)
		
		return result
	}
	
	fun <T> deserializeFromNbt(deserializer: DeserializationStrategy<T>, value: NbtElement): T {
		val input = when (value) {
			is NbtCompound -> NbtDecoder(this, value)
			is AbstractNbtList<*> -> NbtListDecoder(this, value)
			
			else -> NbtPrimitiveDecoder(this, value)
		}
		
		return input.decodeSerializableValue(deserializer)
	}
}