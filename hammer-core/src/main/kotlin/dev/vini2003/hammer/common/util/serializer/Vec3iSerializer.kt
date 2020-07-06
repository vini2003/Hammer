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
import net.minecraft.util.math.Vec3i

object Vec3iSerializer : KSerializer<Vec3i> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("Vec3i") {
			element<Int>("x")
			element<Int>("y")
			element<Int>("z")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Vec3i {
		var vec3i: Vec3i? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val x = decodeIntElement(descriptor, 0)
				val y = decodeIntElement(descriptor, 1)
				val z = decodeIntElement(descriptor, 2)
				
				vec3i = Vec3i(x, y, z)
			}
		} else {
			val x = decoder.decodeInt()
			val y = decoder.decodeInt()
			val z = decoder.decodeInt()
			
			vec3i = Vec3i(x, y, z)
		}
		
		return vec3i!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Vec3i) {
		if (encoder is TaggedEncoder<*>) {
			encoder.encodeStructure(descriptor) {
				encodeIntElement(descriptor, 0, value.x)
				encodeIntElement(descriptor, 1, value.y)
				encodeIntElement(descriptor, 2, value.z)
			}
		} else {
			encoder.encodeInt(value.x)
			encoder.encodeInt(value.y)
			encoder.encodeInt(value.z)
		}
	}
}