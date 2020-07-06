package dev.vini2003.hammer.common.util.extension

import net.minecraft.client.util.math.Vector2f
import net.minecraft.client.util.math.Vector3d
import net.minecraft.nbt.NbtCompound
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

fun NbtCompound.putBlockPos(key: String, value: BlockPos) {
	putLong(key, value.asLong())
}

fun NbtCompound.getBlockPos(key: String): BlockPos? {
	return BlockPos.fromLong(getLong(key))
}

fun NbtCompound.putChunkPos(key: String, value: ChunkPos) {
	putLong(key, value.toLong())
}

fun NbtCompound.getChunkPos(key: String): ChunkPos {
	return ChunkPos(getLong(key))
}

fun NbtCompound.putIdentifier(key: String, value: Identifier) {
	putString(key, value.toString())
}

fun NbtCompound.getIdentifier(key: String): Identifier {
	return Identifier(getString(key))
}

fun <T> NbtCompound.putRegistryKey(key: String, value: RegistryKey<T>) {
	putString("${key}RegistryId", value.method_41185().toString())
	putString("${key}Id", value.value.toString())
}

fun <T> NbtCompound.getRegistryKey(key: String): RegistryKey<Registry<T>> {
	val registryId = getString("${key}RegistryId").toIdentifier()
	val id = getString("${key}Id").toIdentifier()
	
	return RegistryKey.of(RegistryKey.ofRegistry(registryId), id)
}

fun <T> NbtCompound.putTagKey(key: String, value: TagKey<T>) {
	putString("${key}RegistryId", value.registry.value.toString())
	putString("${key}TagId", value.id.toString())
}

fun <T> NbtCompound.getTagKey(key: String): TagKey<Registry<T>> {
	val registryId = getString("${key}RegistryId").toIdentifier()
	val tagId = getString("${key}TagKey").toIdentifier()
	
	return TagKey.of(RegistryKey.ofRegistry(registryId), tagId)
}

fun NbtCompound.putVec2f(key: String, value: Vec2f) {
	putFloat("${key}X", value.x)
	putFloat("${key}Y", value.y)
}

fun NbtCompound.getVec2f(key: String): Vec2f {
	return Vec2f(getFloat("${key}X"), getFloat("${key}Y"))
}

fun NbtCompound.putVec3d(key: String, value: Vec3d) {
	putDouble("${key}X", value.x)
	putDouble("${key}Y", value.y)
	putDouble("${key}Z", value.z)
}

fun NbtCompound.getVec3d(key: String): Vec3d {
	return Vec3d(getDouble("${key}X"), getDouble("${key}Y"), getDouble("${key}Z"))
}

fun NbtCompound.putVec3f(key: String, value: Vec3f) {
	putFloat("${key}X", value.x)
	putFloat("${key}Y", value.y)
	putFloat("${key}Z", value.z)
}

fun NbtCompound.getVec3f(key: String): Vec3f {
	return Vec3f(getFloat("${key}X"), getFloat("${key}Y"), getFloat("${key}Z"))
}

fun NbtCompound.putVec3i(key: String, value: Vec3i) {
	putInt("${key}X", value.x)
	putInt("${key}Y", value.y)
	putInt("${key}Z", value.z)
}

fun NbtCompound.getVec3i(key: String): Vec3i {
	return Vec3i(getInt("${key}X"), getInt("${key}Y"), getInt("${key}Z"))
}

fun NbtCompound.putVector2f(key: String, value: Vector2f) {
	putFloat("${key}X", value.x)
	putFloat("${key}Y", value.y)
}

fun NbtCompound.getVector2f(key: String): Vector2f {
	return Vector2f(getFloat("${key}X"), getFloat("${key}Y"))
}

fun NbtCompound.putVector3d(key: String, value: Vector3d) {
	putDouble("${key}X", value.x)
	putDouble("${key}Y", value.y)
	putDouble("${key}Z", value.z)
}

fun NbtCompound.getVector3d(key: String): Vector3d {
	return Vector3d(getDouble("${key}X"), getDouble("${key}Y"), getDouble("${key}Z"))
}

fun NbtCompound.putVector4f(key: String, value: Vector4f) {
	putFloat("${key}X", value.x)
	putFloat("${key}Y", value.y)
	putFloat("${key}Z", value.z)
	putFloat("${key}W", value.w)
}

fun NbtCompound.getVector4f(key: String): Vector4f {
	return Vector4f(getFloat("${key}X"), getFloat("${key}Y"), getFloat("${key}Z"), getFloat("${key}W"))
}

fun NbtCompound.putQuaternion(key: String, value: Quaternion) {
	putFloat("${key}X", value.x)
	putFloat("${key}Y", value.y)
	putFloat("${key}Z", value.z)
	putFloat("${key}W", value.w)
}

fun NbtCompound.getQuaternion(key: String): Quaternion {
	return Quaternion(getFloat("${key}X"), getFloat("${key}Y"), getFloat("${key}Z"), getFloat("${key}W"))
}