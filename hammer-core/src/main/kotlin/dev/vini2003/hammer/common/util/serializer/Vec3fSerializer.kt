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
import net.minecraft.util.math.Vec3f

object Vec3fSerializer : KSerializer<Vec3f> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("Vec3f") {
			element<Float>("x")
			element<Float>("y")
			element<Float>("z")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Vec3f {
		var vec3f: Vec3f? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val x = decodeFloatElement(descriptor, 0)
				val y = decodeFloatElement(descriptor, 1)
				val z = decodeFloatElement(descriptor, 2)
				
				vec3f = Vec3f(x, y, z)
			}
		} else {
			val x = decoder.decodeFloat()
			val y = decoder.decodeFloat()
			val z = decoder.decodeFloat()
			
			vec3f = Vec3f(x, y, z)
		}
		
		return vec3f!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Vec3f) {
		if (encoder is TaggedEncoder<*>) {
			encoder.encodeStructure(descriptor) {
				encodeFloatElement(descriptor, 0, value.x)
				encodeFloatElement(descriptor, 1, value.y)
				encodeFloatElement(descriptor, 2, value.z)
			}
		} else {
			encoder.encodeFloat(value.x)
			encoder.encodeFloat(value.y)
			encoder.encodeFloat(value.z)
		}
	}
}