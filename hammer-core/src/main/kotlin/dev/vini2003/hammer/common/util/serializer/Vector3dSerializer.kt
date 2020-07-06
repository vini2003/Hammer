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
import net.minecraft.client.util.math.Vector3d

object Vector3dSerializer : KSerializer<Vector3d> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("Vector3d") {
			element<Double>("x")
			element<Double>("y")
			element<Double>("z")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): Vector3d {
		var vector3d: Vector3d? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val x = decodeDoubleElement(descriptor, 0)
				val y = decodeDoubleElement(descriptor, 1)
				val z = decodeDoubleElement(descriptor, 2)
				
				vector3d = Vector3d(x, y, z)
			}
		} else {
			val x = decoder.decodeDouble()
			val y = decoder.decodeDouble()
			val z = decoder.decodeDouble()
			
			vector3d = Vector3d(x, y, z)
		}
		
		return vector3d!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: Vector3d) {
		if (encoder is TaggedEncoder<*>) {
			encoder.encodeStructure(descriptor) {
				encodeDoubleElement(descriptor, 0, value.x)
				encodeDoubleElement(descriptor, 1, value.y)
				encodeDoubleElement(descriptor, 2, value.z)
			}
		} else {
			encoder.encodeDouble(value.x)
			encoder.encodeDouble(value.y)
			encoder.encodeDouble(value.z)
		}
	}
}