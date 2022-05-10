package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtDouble

@Serializer(forClass = NbtDouble::class)
object NbtDoubleSerializer : KSerializer<NbtDouble> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NbtDouble", PrimitiveKind.DOUBLE)
	
	override fun deserialize(decoder: Decoder): NbtDouble {
		return NbtDouble.of(decoder.decodeDouble())
	}
	
	override fun serialize(encoder: Encoder, value: NbtDouble) {
		return encoder.encodeDouble(value.doubleValue())
	}
}