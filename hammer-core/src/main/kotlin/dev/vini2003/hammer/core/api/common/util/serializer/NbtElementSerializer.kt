package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.*
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtElement

@OptIn(InternalSerializationApi::class)
@Serializer(forClass = NbtElement::class)
object NbtElementSerializer : KSerializer<NbtElement> {
	override val descriptor: SerialDescriptor = buildSerialDescriptor("NbtEleme]", PolymorphicKind.OPEN) {
		element("type", String.serializer().descriptor)
		element("value", buildSerialDescriptor("Nbt${NbtElement::class.simpleName}", SerialKind.CONTEXTUAL))
	}
	
	override fun deserialize(decoder: Decoder): NbtElement {
		return PolymorphicSerializer(NbtElement::class).deserialize(decoder)
	}
	
	override fun serialize(encoder: Encoder, value: NbtElement) {
		PolymorphicSerializer(NbtElement::class).serialize(encoder, value)
	}
}