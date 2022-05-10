package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtLong

@Serializer(forClass = NbtLong::class)
object NbtLongSerializer : KSerializer<NbtLong> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NbtLong", PrimitiveKind.LONG)
	
	override fun deserialize(decoder: Decoder): NbtLong {
		return NbtLong.of(decoder.decodeLong())
	}
	
	override fun serialize(encoder: Encoder, value: NbtLong) {
		encoder.encodeLong(value.longValue())
	}
}