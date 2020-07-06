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
import net.minecraft.util.math.Quaternion

object QuaternionSerializer : KSerializer<Quaternion> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("Quaternion") {
			element<Float>("x")
			element<Float>("y")
			element<Float>("z")
			element<Float>("w")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Quaternion {
		var quaternion: Quaternion? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val x = decodeFloatElement(descriptor, 0)
				val y = decodeFloatElement(descriptor, 1)
				val z = decodeFloatElement(descriptor, 2)
				val w = decodeFloatElement(descriptor, 3)
				
				quaternion = Quaternion(x, y, z, w)
			}
		} else {
			val x = decoder.decodeFloat()
			val y = decoder.decodeFloat()
			val z = decoder.decodeFloat()
			val w = decoder.decodeFloat()
			
			quaternion = Quaternion(x, y, z, w)
		}
		
		return quaternion!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Quaternion) {
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