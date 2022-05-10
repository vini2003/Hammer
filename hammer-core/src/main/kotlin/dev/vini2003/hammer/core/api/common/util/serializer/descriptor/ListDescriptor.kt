package dev.vini2003.hammer.core.api.common.util.serializer.descriptor

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind

@ExperimentalSerializationApi
class ListDescriptor(
	override val serialName: String,
	val elementDescriptor: SerialDescriptor
) : SerialDescriptor {
	override val elementsCount: Int = 1
	
	override val kind: SerialKind = StructureKind.LIST
	
	override fun getElementAnnotations(index: Int): List<Annotation> {
		if (index != 0) {
			throw IndexOutOfBoundsException("List descriptor only has one child element!")
		}
		
		return emptyList()
	}
	
	override fun getElementDescriptor(index: Int): SerialDescriptor {
		if (index != 0) {
			throw IndexOutOfBoundsException("List descriptor only has one child element!")
		}
		
		return elementDescriptor
	}
	
	override fun getElementIndex(name: String): Int {
		if (name.toInt() != 0) {
			throw IndexOutOfBoundsException("$name is not a valid list index!")
		}
		
		return name.toInt()
	}
	
	override fun getElementName(index: Int): String {
		if (index != 0) {
			throw IndexOutOfBoundsException("$index is not a valid list index!")
		}
		
		return index.toString()
	}
	
	override fun isElementOptional(index: Int): Boolean {
		return false
	}
}