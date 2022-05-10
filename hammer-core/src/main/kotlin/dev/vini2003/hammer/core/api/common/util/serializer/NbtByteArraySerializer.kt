package dev.vini2003.hammer.core.api.common.util.serializer

import dev.vini2003.hammer.core.api.common.util.serializer.descriptor.ListDescriptor
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtByte
import net.minecraft.nbt.NbtByteArray

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = NbtByteArray::class)
object NbtByteArraySerializer : KSerializer<NbtByteArray> {
	override val descriptor: SerialDescriptor = ListDescriptor("NbtByteArray", NbtByteSerializer.descriptor)
	
	private val listSerializer: KSerializer<List<NbtByte>> = ListSerializer(NbtByteSerializer)
	
	override fun deserialize(decoder: Decoder): NbtByteArray {
		return NbtByteArray(listSerializer.deserialize(decoder).map { byte -> byte.byteValue() })
	}
	
	override fun serialize(encoder: Encoder, value: NbtByteArray) {
		listSerializer.serialize(encoder, value)
	}
}