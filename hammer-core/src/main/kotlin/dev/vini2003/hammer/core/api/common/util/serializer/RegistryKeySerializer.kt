/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.core.api.common.util.serializer

import dev.vini2003.hammer.core.api.common.util.extension.toIdentifier
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
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

@Serializer(forClass = RegistryKey::class)
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
