package dev.vini2003.hammer.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.util.math.ChunkPos

object ChunkPosSerializer : KSerializer<ChunkPos> {
	override val descriptor: SerialDescriptor =
		PrimitiveSerialDescriptor("ChunkPos", PrimitiveKind.LONG)
	
	override fun deserialize(decoder: Decoder): ChunkPos {
		return ChunkPos(decoder.decodeLong())
	}
	
	override fun serialize(encoder: Encoder, value: ChunkPos) {
		encoder.encodeLong(value.toLong())
	}
}