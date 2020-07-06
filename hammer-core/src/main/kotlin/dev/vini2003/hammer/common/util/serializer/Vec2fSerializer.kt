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
import net.minecraft.util.math.Vec2f

object Vec2fSerializer : KSerializer<Vec2f> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("Vec2f") {
			element<Float>("x")
			element<Float>("y")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Vec2f {
		var vec2f: Vec2f? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val x = decodeFloatElement(descriptor, 0)
				val y = decodeFloatElement(descriptor, 1)
				
				vec2f = Vec2f(x, y)
			}
		} else {
			val x = decoder.decodeFloat()
			val y = decoder.decodeFloat()
			
			vec2f = Vec2f(x, y)
		}
		
		return vec2f!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Vec2f) {
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