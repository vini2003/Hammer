package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtInt

@Serializer(forClass = NbtInt::class)
object NbtIntSerializer : KSerializer<NbtInt> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NbtInt", PrimitiveKind.INT)
	
	override fun deserialize(decoder: Decoder): NbtInt {
		return NbtInt.of(decoder.decodeInt())
	}
	
	override fun serialize(encoder: Encoder, value: NbtInt) {
		encoder.encodeInt(value.intValue())
	}
}