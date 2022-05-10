package dev.vini2003.hammer.core.api.common.util.serializer.format

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.internal.NamedValueDecoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.*

internal inline fun <reified T : NbtElement> cast(obj: NbtElement): T {
	check(obj is T) { "Expected ${T::class} but found ${obj::class}" }
	
	return obj
}

@OptIn(InternalSerializationApi::class)
sealed class AbstractNbtDecoder(
	val format: Nbt,
	open val map: NbtElement
) : NamedValueDecoder() {
	override val serializersModule: SerializersModule
		get() = format.serializersModule
	
	abstract fun getElement(tag: String): NbtElement
	
	fun getCurrentElement(): NbtElement {
		val tag = currentTagOrNull
		
		if (tag == null) {
			return map
		} else {
			return getElement(tag)
		}
	}
	
	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		val currentObject = getCurrentElement()
		
		return when (descriptor.kind) {
			StructureKind.LIST -> NbtListDecoder(format, cast(currentObject))
			StructureKind.MAP -> {
				when {
					descriptor.kind is PrimitiveKind || descriptor.kind == StructureKind.MAP -> NbtMapDecoder(format, cast(currentObject))
					
					else -> NbtListDecoder(format, cast(currentObject))
				}
			}
			
			
			is PolymorphicKind -> NbtMapDecoder(format, cast(currentObject))
			
			else -> NbtDecoder(format, cast(currentObject))
		}
	}
	
	override fun decodeTaggedChar(tag: String): Char {
		val element = getElement(tag)
		val elementAsString = element.asString()
		
		if (elementAsString.length == 1) {
			return elementAsString[0]
		} else {
			throw SerializationException("${element} cannot be decoded as Char")
		}
	}
	
	override fun decodeTaggedEnum(tag: String, enumDescriptor: SerialDescriptor): Int {
		val element = getElement(tag)
		val elementAsString = element.asString()
		
		return enumDescriptor.getElementIndex(elementAsString)
	}
	
	override fun decodeTaggedBoolean(tag: String): Boolean {
		return decodeTaggedByte(tag) == 1.toByte()
	}
	
	override fun decodeTaggedByte(tag: String): Byte {
		return decodeTaggedNumber(tag, { byteValue() }, { toByte() })
	}
	
	override fun decodeTaggedShort(tag: String): Short {
		return decodeTaggedNumber(tag, { shortValue() }, { toShort() })
	}
	
	override fun decodeTaggedInt(tag: String): Int {
		return decodeTaggedNumber(tag, { intValue() }, { toInt() })
	}
	
	override fun decodeTaggedLong(tag: String): Long {
		return decodeTaggedNumber(tag, { longValue() }, { toLong() })
	}
	
	override fun decodeTaggedFloat(tag: String): Float {
		return decodeTaggedNumber(tag, { floatValue() }, { toFloat() })
	}
	
	override fun decodeTaggedDouble(tag: String): Double {
		return decodeTaggedNumber(tag, { doubleValue() }, { toDouble() })
	}
	
	override fun decodeTaggedString(tag: String): String {
		return getElement(tag).asString()
	}
	
	fun decodeTaggedNbt(tag: String): NbtElement {
		return getElement(tag)
	}
	
	fun decodeTag(): NbtElement {
		return decodeTaggedNbt(popTag())
	}
	
	fun <T> decodeTaggedNumber(
		tag: String,
		getter: AbstractNbtNumber.() -> T,
		stringGetter: String.() -> T
	): T {
		val element = getElement(tag)
		
		if (element is AbstractNbtNumber) {
			return element.getter()
		} else {
			return element.asString().stringGetter()
		}
	}
}

class NbtPrimitiveDecoder(
	format: Nbt,
	override val map: NbtElement
) : AbstractNbtDecoder(format, map) {
	init {
		pushTag("primitive")
	}
	
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		return 0
	}
	
	override fun getElement(tag: String): NbtElement {
		require(tag == "primitive") { "Expected NBT Primitive, found non-primitive $tag"}
		
		return map
	}
}

@OptIn(ExperimentalSerializationApi::class)
open class NbtDecoder(
	format: Nbt,
	override val map: NbtCompound
) : AbstractNbtDecoder(format, map) {
	private var pos: Int = 0
	
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		while (pos < descriptor.elementsCount) {
			val name = descriptor.getTag(pos++)
			
			if (map.contains(name)) {
				return pos - 1
			}
		}
		
		return CompositeDecoder.DECODE_DONE
	}
	
	override fun getElement(tag: String): NbtElement {
		return map.get(tag)!!
	}
}

class NbtMapDecoder(
	format: Nbt,
	override val map: NbtCompound
) : NbtDecoder(format, map) {
	private val keys: List<String> = map.keys.toList()
	
	private val size: Int = keys.size * 2
	
	private var pos: Int = -1
	
	override fun elementName(desc: SerialDescriptor, index: Int): String {
		val i = index / 2
		
		return keys[i]
	}
	
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		while (pos < size - 1) {
			pos += 1
			
			return pos
		}
		
		return CompositeDecoder.DECODE_DONE
	}
	
	override fun getElement(tag: String): NbtElement {
		if (pos % 2 == 0) {
			return NbtString.of(tag)
		} else {
			return map.get(tag)!!
		}
	}
	
	override fun endStructure(descriptor: SerialDescriptor) {
	}
}

class NbtListDecoder(
	format: Nbt,
	override val map: AbstractNbtList<*>
) : AbstractNbtDecoder(format, map) {
	private val size: Int = map.size
	
	private var pos = -1
	
	override fun elementName(desc: SerialDescriptor, index: Int): String {
		return index.toString()
	}
	
	override fun getElement(tag: String): NbtElement {
		return map[tag.toInt()]
	}
	
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		while (pos < size - 1) {
			pos++
			
			return pos
		}
		
		return CompositeDecoder.DECODE_DONE
	}
}