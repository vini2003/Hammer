package dev.vini2003.hammer.common.util.serializer

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.internal.TaggedDecoder
import kotlinx.serialization.internal.TaggedEncoder
import net.minecraft.client.util.math.Vector2f

object Vector2fSerializer : KSerializer<Vector2f> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("Vector2f") {
			element<Float>("x")
			element<Float>("y")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Vector2f {
		var vector2f: Vector2f? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val x = decodeFloatElement(descriptor, 0)
				val y = decodeFloatElement(descriptor, 1)
				
				vector2f = Vector2f(x, y)
			}
		} else {
			val x = decoder.decodeFloat()
			val y = decoder.decodeFloat()
			
			vector2f = Vector2f(x, y)
		}
		
		return vector2f!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Vector2f) {
		if (encoder is TaggedEncoder<*>) {
			encoder.encodeStructure(descriptor) {
				encodeFloatElement(descriptor, 0, value.x)
				encodeFloatElement(descriptor, 1, value.y)
			}
		} else {
			encoder.encodeFloat(value.x)
			encoder.encodeFloat(value.y)
		}
	}
}