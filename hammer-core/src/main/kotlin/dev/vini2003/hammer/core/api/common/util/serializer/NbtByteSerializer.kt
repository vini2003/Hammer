package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtByte

@Serializer(forClass = NbtByte::class)
object NbtByteSerializer : KSerializer<NbtByte> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NbtByte", PrimitiveKind.BYTE)
	
	override fun deserialize(decoder: Decoder): NbtByte {
		return NbtByte.of(decoder.decodeByte())
	}
	
	override fun serialize(encoder: Encoder, value: NbtByte) {
		encoder.encodeByte(value.byteValue())
	}
}