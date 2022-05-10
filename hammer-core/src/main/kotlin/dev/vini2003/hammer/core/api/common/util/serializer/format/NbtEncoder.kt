package dev.vini2003.hammer.core.api.common.util.serializer.format

import dev.vini2003.hammer.core.api.common.exception.NbtEncodeException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.internal.NamedValueEncoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.*

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
sealed class AbstractNbtEncoder(
	val format: Nbt,
	val resultConsumer: (NbtElement) -> Unit
) : NamedValueEncoder() {
	override val serializersModule: SerializersModule
		get() = format.serializersModule
	
	override fun composeName(parentName: String, childName: String): String {
		return childName
	}
	
	abstract fun putElement(tag: String, element: NbtElement)
	
	abstract fun getCurrentELement(): NbtElement
	
	override fun encodeTaggedChar(tag: String, value: Char) {
		putElement(tag, NbtString.of(value.toString()))
	}
	
	override fun encodeTaggedEnum(
		tag: String,
		enumDescriptor: SerialDescriptor,
		ordinal: Int
	) {
		putElement(tag, NbtString.of(enumDescriptor.getElementName(ordinal)))
	}
	
	override fun encodeTaggedBoolean(tag: String, value: Boolean) {
		putElement(tag, NbtByte.of(value))
	}
	
	override fun encodeTaggedByte(tag: String, value: Byte) {
		putElement(tag, NbtByte.of(value))
	}
	
	override fun encodeTaggedShort(tag: String, value: Short) {
		putElement(tag, NbtShort.of(value))
	}
	
	override fun encodeTaggedInt(tag: String, value: Int) {
		putElement(tag, NbtInt.of(value))
	}
	
	override fun encodeTaggedLong(tag: String, value: Long) {
		putElement(tag, NbtLong.of(value))
	}
	
	override fun encodeTaggedFloat(tag: String, value: Float) {
		putElement(tag, NbtFloat.of(value))
	}
	
	override fun encodeTaggedDouble(tag: String, value: Double) {
		putElement(tag, NbtDouble.of(value))
	}
	
	override fun encodeTaggedString(tag: String, value: String) {
		putElement(tag, NbtString.of(value))
	}
	
	override fun encodeTaggedValue(tag: String, value: Any) {
		putElement(tag, NbtString.of(value.toString()))
	}
	
	fun encodeTaggedTag(key: String, tag: NbtElement) {
		putElement(key, tag)
	}
	
	override fun elementName(descriptor: SerialDescriptor, index: Int): String {
		if (descriptor.kind is PolymorphicKind) {
			return index.toString()
		} else {
			return super.elementName(descriptor, index)
		}
	}
	
	override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
		val consumer: (NbtElement) -> Unit
		
		if (currentTagOrNull == null) {
			consumer = this.resultConsumer
		} else {
			consumer = { node -> putElement(currentTag, node)}
		}
		
		val encoder = when (descriptor.kind) {
			StructureKind.LIST -> NbtListEncoder(format, consumer)
			StructureKind.MAP -> {
				when {
					descriptor.kind is PrimitiveKind || descriptor.kind == StructureKind.MAP -> NbtMapEncoder(format, consumer)
					
					else -> NbtListEncoder(format, consumer)
				}
			}
			
			is PolymorphicKind -> NbtMapEncoder(format, consumer)

			else -> NbtEncoder(format, consumer)
		}
		
		return encoder
	}
	
	override fun endEncode(descriptor: SerialDescriptor) {
		resultConsumer(getCurrentELement())
	}
}

open class NbtEncoder(
	format: Nbt,
	endConsumer: (NbtElement) -> Unit
) : AbstractNbtEncoder(format, endConsumer) {
	protected val content: NbtCompound = NbtCompound()
	
	override fun putElement(tag: String, element: NbtElement) {
		content.put(tag, element)
	}
	
	override fun getCurrentELement(): NbtElement {
		return content
	}
}

private class NbtMapEncoder(format: Nbt, nodeConsumer: (NbtElement) -> Unit) : NbtEncoder(format, nodeConsumer) {
	private lateinit var key: String
	
	override fun putElement(tag: String, element: NbtElement) {
		val pos = tag.toInt()
		
		when {
			pos % 2 == 0 -> this.key = when (element) {
				is NbtCompound, is AbstractNbtList<*> -> throw NbtEncodeException("Found invalid key in NBT map encoding - required a primitive type but found compound type [${element::class.simpleName}], primitive types are: ${NbtPrimitiveTypes}")
				
				else -> element.asString()
			}
			
			else -> content[this.key] = element
		}
	}
	
	override fun getCurrentELement(): NbtElement {
		return content
	}
}

private operator fun NbtCompound.set(key: String, value: NbtElement) {
	put(key, value)
}

private fun NbtList.addAnyTag(pos: Int, tag: NbtElement) {
	value.add(pos, tag)
}

private class NbtListEncoder(
	json: Nbt,
	endConsumer: (NbtElement) -> Unit
) : AbstractNbtEncoder(json, endConsumer) {
	private val list: NbtList = NbtList()
	
	override fun elementName(descriptor: SerialDescriptor, index: Int): String {
		return index.toString()
	}
	
	override fun putElement(tag: String, element: NbtElement) {
		val pos = tag.toInt()
		
		list.addAnyTag(pos, element)
	}
	
	override fun getCurrentELement(): NbtElement {
		return list
	}
}