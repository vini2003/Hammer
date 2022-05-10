package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtString

@Serializer(forClass = NbtString::class)
object NbtStringSerializer : KSerializer<NbtString> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NbtString", PrimitiveKind.STRING)
	
	override fun deserialize(decoder: Decoder): NbtString {
		return NbtString.of(decoder.decodeString())
	}
	
	override fun serialize(encoder: Encoder, value: NbtString) {
		encoder.encodeString(value.asString())
	}
}