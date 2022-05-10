package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtShort

@Serializer(forClass = NbtShort::class)
object NbtShortSerializer : KSerializer<NbtShort> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NbtShort", PrimitiveKind.SHORT)
	
	override fun deserialize(decoder: Decoder): NbtShort {
		return NbtShort.of(decoder.decodeShort())
	}
	
	override fun serialize(encoder: Encoder, value: NbtShort) {
		encoder.encodeShort(value.shortValue())
	}
}