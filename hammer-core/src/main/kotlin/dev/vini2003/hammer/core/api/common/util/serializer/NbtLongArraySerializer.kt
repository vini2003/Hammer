package dev.vini2003.hammer.core.api.common.util.serializer

import dev.vini2003.hammer.core.api.common.util.serializer.descriptor.ListDescriptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.LongArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtLong
import net.minecraft.nbt.NbtLongArray

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = NbtLongArray::class)
object NbtLongArraySerializer : KSerializer<NbtLongArray> {
	override val descriptor: SerialDescriptor = ListDescriptor("NbtLongArray", NbtLongSerializer.descriptor)
	
	private val listSerializer: KSerializer<List<NbtLong>> = ListSerializer(NbtLongSerializer)
	
	override fun deserialize(decoder: Decoder): NbtLongArray {
		return NbtLongArray(listSerializer.deserialize(decoder).map { Long -> Long.longValue() })
	}
	
	override fun serialize(encoder: Encoder, value: NbtLongArray) {
		listSerializer.serialize(encoder, value)
	}
}