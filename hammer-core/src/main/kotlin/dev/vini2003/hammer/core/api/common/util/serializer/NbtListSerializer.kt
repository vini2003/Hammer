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
import net.minecraft.nbt.*

@OptIn(ExperimentalSerializationApi::class)
@Serializer(NbtList::class)
object NbtListSerializer : KSerializer<NbtList> {
	override val descriptor: SerialDescriptor = ListDescriptor("NbtList", NbtElementSerializer.descriptor)
	
	private val listSerializer: KSerializer<List<NbtElement>> = ListSerializer(NbtElementSerializer)
	
	override fun deserialize(decoder: Decoder): NbtList {
		val list = NbtList()
		
		listSerializer.deserialize(decoder).forEach { element ->
			list.add(element)
		}
		
		return list
	}
	
	override fun serialize(encoder: Encoder, value: NbtList) {
		listSerializer.serialize(encoder, value)
	}
}