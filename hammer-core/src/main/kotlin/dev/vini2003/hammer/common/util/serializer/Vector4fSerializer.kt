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
import net.minecraft.util.math.Vector4f

object Vector4fSerializer : KSerializer<Vector4f> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("Vector4f") {
			element<Float>("x")
			element<Float>("y")
			element<Float>("z")
			element<Float>("w")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Vector4f {
		var vector4f: Vector4f? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val x = decodeFloatElement(descriptor, 0)
				val y = decodeFloatElement(descriptor, 1)
				val z = decodeFloatElement(descriptor, 2)
				val w = decodeFloatElement(descriptor, 3)
				
				vector4f = Vector4f(x, y, z, w)
			}
		} else {
			val x = decoder.decodeFloat()
			val y = decoder.decodeFloat()
			val z = decoder.decodeFloat()
			val w = decoder.decodeFloat()
			
			vector4f = Vector4f(x, y, z, w)
		}
		
		return vector4f!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Vector4f) {
		if (encoder is TaggedEncoder<*>) {
			encoder.encodeStructure(descriptor) {
				encodeFloatElement(descriptor, 0, value.x)
				encodeFloatElement(descriptor, 1, value.y)
				encodeFloatElement(descriptor, 2, value.z)
				encodeFloatElement(descriptor, 3, value.w)
			}
		} else {
			encoder.encodeFloat(value.x)
			encoder.encodeFloat(value.y)
			encoder.encodeFloat(value.z)
			encoder.encodeFloat(value.w)
		}
	}
}