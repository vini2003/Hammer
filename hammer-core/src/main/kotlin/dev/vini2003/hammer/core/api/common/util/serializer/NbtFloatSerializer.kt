package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtFloat

@Serializer(forClass = NbtFloat::class)
object NbtFloatSerializer : KSerializer<NbtFloat> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NbtFloat", PrimitiveKind.FLOAT)
	
	override fun deserialize(decoder: Decoder): NbtFloat {
		return NbtFloat.of(decoder.decodeFloat())
	}
	
	override fun serialize(encoder: Encoder, value: NbtFloat) {
		encoder.encodeFloat(value.floatValue())
	}
}