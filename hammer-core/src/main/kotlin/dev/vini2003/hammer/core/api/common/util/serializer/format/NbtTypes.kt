package dev.vini2003.hammer.core.api.common.util.serializer.format

import net.minecraft.nbt.*
import kotlin.reflect.KClass

enum class NbtTypes(
	val `class`: KClass<*>
) {
	BYTE(NbtByte::class),
	SHORT(NbtShort::class),
	INT(NbtInt::class),
	LONG(NbtLong::class),
	FLOAT(NbtFloat::class),
	DOUBLE(NbtDouble::class),
	BYTE_ARRAY(ByteArray::class),
	STRING(NbtString::class),
	LIST(NbtList::class),
	COMPOUND(NbtCompound::class),
	INT_ARRAY(NbtIntArray::class),
	LONG_ARRAY(NbtLongArray::class);
	
	companion object {
		override fun toString(): String {
			return values().map { value -> "${value::`class`.javaClass.simpleName}" }.joinToString(",\n", prefix = "[", postfix = "]")
		}
	}
}

enum class NbtPrimitiveTypes(
	val `class`: KClass<*>
) {
	BYTE(NbtByte::class),
	SHORT(NbtShort::class),
	INT(NbtInt::class),
	LONG(NbtLong::class),
	FLOAT(NbtFloat::class),
	DOUBLE(NbtDouble::class),
	STRING(NbtString::class);
	
	companion object {
		override fun toString(): String {
			return values().map { value -> "${value::`class`.javaClass.simpleName}" }.joinToString(",\n", prefix = "[", postfix = "]")
		}
	}
}

enum class NbtCompoundTypes(
	val `class`: KClass<*>
) {
	BYTE_ARRAY(ByteArray::class),
	LIST(NbtList::class),
	COMPOUND(NbtCompound::class),
	INT_ARRAY(NbtIntArray::class),
	LONG_ARRAY(NbtLongArray::class);
	
	companion object {
		override fun toString(): String {
			return values().map { value -> "${value::`class`.javaClass.simpleName}" }.joinToString(",\n", prefix = "[", postfix = "]")
		}
	}
}