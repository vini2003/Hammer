package dev.vini2003.hammer.common.util.serializer

import kotlinx.serialization.ContextualSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

open class UnsupportedSerializer<T> : KSerializer<T> {
	override val descriptor: SerialDescriptor = ContextualSerializer(Any::class, null, emptyArray()).descriptor
	
	override fun deserialize(decoder: Decoder): T {
		throw UnsupportedOperationException()
	}
	
	override fun serialize(encoder: Encoder, value: T) {
		throw UnsupportedOperationException()
	}
}