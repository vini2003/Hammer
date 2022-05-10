package dev.vini2003.hammer.core.api.common.util.serializer.module

import dev.vini2003.hammer.core.api.common.util.serializer.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import net.minecraft.client.util.math.Vector2f
import net.minecraft.client.util.math.Vector3d
import net.minecraft.nbt.*
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryKey
import java.util.*

val HammerSerializersModule = SerializersModule {
	contextual(BlockPos::class, BlockPosSerializer)
	contextual(ChunkPos::class, ChunkPosSerializer)
	contextual(Identifier::class, IdentifierSerializer)
	contextual(Quaternion::class, QuaternionSerializer)
	contextual(RegistryKey::class, { types -> RegistryKeySerializer(types.first()) })
	contextual(TagKey::class, { types -> TagKeySerializer(types.first() ) })
	contextual(UUID::class, UUIDSerializer)
	contextual(Vec2f::class, Vec2fSerializer)
	contextual(Vec3d::class, Vec3dSerializer)
	contextual(Vec3f::class, Vec3fSerializer)
	contextual(Vec3i::class, Vec3iSerializer)
	contextual(Vector2f::class, Vector2fSerializer)
	contextual(Vector3d::class, Vector3dSerializer)
	contextual(Vector4f::class, Vector4fSerializer)
	
	polymorphic(NbtElement::class) {
		subclass(NbtByte::class, NbtByteSerializer)
		subclass(NbtShort::class, NbtShortSerializer)
		subclass(NbtInt::class, NbtIntSerializer)
		subclass(NbtLong::class, NbtLongSerializer)
		subclass(NbtFloat::class, NbtFloatSerializer)
		subclass(NbtDouble::class, NbtDoubleSerializer)
		subclass(NbtByteArray::class, NbtByteArraySerializer)
		subclass(NbtString::class, NbtStringSerializer)
		subclass(NbtList::class, NbtListSerializer)
		subclass(NbtCompound::class, NbtCompoundSerializer)
		subclass(NbtIntArray::class, NbtIntArraySerializer)
		subclass(NbtLongArray::class, NbtLongArraySerializer)
	}
}