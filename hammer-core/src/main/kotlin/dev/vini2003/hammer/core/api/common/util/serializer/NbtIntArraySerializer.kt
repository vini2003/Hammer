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
import net.minecraft.nbt.NbtInt
import net.minecraft.nbt.NbtIntArray

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = NbtIntArray::class)
object NbtIntArraySerializer : KSerializer<NbtIntArray> {
	override val descriptor: SerialDescriptor = ListDescriptor("NbtIntArray", NbtIntSerializer.descriptor)
	
	private val listSerializer: KSerializer<List<NbtInt>> = ListSerializer(NbtIntSerializer)
	
	override fun deserialize(decoder: Decoder): NbtIntArray {
		return NbtIntArray(listSerializer.deserialize(decoder).map { byte -> byte.intValue() })
	}
	
	override fun serialize(encoder: Encoder, value: NbtIntArray) {
		listSerializer.serialize(encoder, value)
	}
}