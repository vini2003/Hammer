package dev.vini2003.hammer.core.api.common.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.mapSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtInt

@Serializer(forClass = NbtCompound::class)
object NbtCompoundSerializer : KSerializer<NbtCompound> {
	override val descriptor: SerialDescriptor = mapSerialDescriptor(PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING), NbtElementSerializer.descriptor)
	
	private val mapSerializer = MapSerializer(String.serializer(), NbtElementSerializer)
	
	override fun deserialize(decoder: Decoder): NbtCompound {
		val compound = NbtCompound()
		
		mapSerializer.deserialize(decoder).forEach { (key, value) ->
			compound.put(key, value)
		}
		
		return compound
	}
	
	
	override fun serialize(encoder: Encoder, value: NbtCompound) {
		mapSerializer.serialize(encoder, value.keys.map { key -> key to value.get(key)!! }.toMap())
	}
}