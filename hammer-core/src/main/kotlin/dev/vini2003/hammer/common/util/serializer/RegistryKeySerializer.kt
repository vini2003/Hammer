package dev.vini2003.hammer.common.util.serializer

import dev.vini2003.hammer.common.util.extension.toIdentifier
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.internal.TaggedDecoder
import kotlinx.serialization.internal.TaggedEncoder
import net.minecraft.util.registry.RegistryKey

class RegistryKeySerializer<T>(val unsupportedSerializer: KSerializer<T>) : KSerializer<RegistryKey<@Serializable(UnsupportedSerializer::class) T>> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("RegistryKey") {
			element<String>("registry")
			element<String>("value")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): RegistryKey<T> {
		var registryKey: RegistryKey<T>? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val registryId = decodeStringElement(descriptor, 0).toIdentifier()
				val tagId = decodeStringElement(descriptor, 1).toIdentifier()
				
				registryKey = RegistryKey.of(RegistryKey.ofRegistry<T>(registryId), tagId)
			}
		} else {
			val registryId = decoder.decodeString().toIdentifier()
			val tagId = decoder.decodeString().toIdentifier()
			
			registryKey = RegistryKey.of(RegistryKey.ofRegistry<T>(registryId), tagId)
		}
		
		return registryKey!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: RegistryKey<T>) {
		if (encoder is TaggedEncoder<*>) {
			encoder.encodeStructure(descriptor) {
				encodeStringElement(descriptor, 0, value.method_41185().toString())
				encodeStringElement(descriptor, 1, value.value.toString())
			}
		} else {
			encoder.encodeString(value.method_41185().toString())
			encoder.encodeString(value.value.toString())
		}
	}
}
