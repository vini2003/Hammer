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
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.RegistryKey

class TagKeySerializer<T>(val unsupportedSerializer: KSerializer<T>) : KSerializer<TagKey<@Serializable(UnsupportedSerializer::class) T>> {
	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("TagKey") {
			element<String>("registry")
			element<String>("value")
		}
	
	@OptIn(InternalSerializationApi::class)
	override fun deserialize(decoder: Decoder): TagKey<T> {
		var tagKey: TagKey<T>? = null
		
		if (decoder is TaggedDecoder<*>) {
			decoder.decodeStructure(descriptor) {
				val registryId = decodeStringElement(descriptor, 0).toIdentifier()
				val tagId = decodeStringElement(descriptor, 1).toIdentifier()
				
				tagKey = TagKey.of(RegistryKey.ofRegistry(registryId), tagId)
			}
		} else {
			val registryId = decoder.decodeString().toIdentifier()
			val tagId = decoder.decodeString().toIdentifier()
			
			tagKey = TagKey.of(RegistryKey.ofRegistry(registryId), tagId)
		}
		
		
		return tagKey!!
	}
	
	@OptIn(InternalSerializationApi::class)
	override fun serialize(encoder: Encoder, value: TagKey<T>) {
		if (encoder is TaggedEncoder<*>) {
			encoder.encodeStructure(descriptor) {
				encodeStringElement(descriptor, 0, value.registry.value.toString())
				encodeStringElement(descriptor, 1, value.id.toString())
			}
		} else {
			encoder.encodeString(value.registry.value.toString())
			encoder.encodeString(value.id.toString())
		}
	}
}
