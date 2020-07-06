package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.common.util.extension.*
import net.minecraft.client.util.math.Vector2f
import net.minecraft.client.util.math.Vector3d
import net.minecraft.nbt.NbtCompound
import net.minecraft.tag.TagKey
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryKey

object NbtUtils {
	@JvmStatic
	fun <T> putRegistryKey(nbt: NbtCompound, key: String, value: RegistryKey<T>) = nbt.putRegistryKey(key, value)
	
	@JvmStatic
	fun <T> getRegistryKey(nbt: NbtCompound, key: String) = nbt.getRegistryKey<T>(key)
	
	@JvmStatic
	fun <T> putTagKey(nbt: NbtCompound, key: String, value: TagKey<T>) = nbt.putTagKey(key, value)
	
	@JvmStatic
	fun <T> getTagKey(nbt: NbtCompound, key: String) = nbt.getTagKey<T>(key)
	
	@JvmStatic
	fun putVec2f(nbt: NbtCompound, key: String, value: Vec2f) = nbt.putVec2f(key, value)
	
	@JvmStatic
	fun getVec2f(nbt: NbtCompound, key: String) = nbt.getVec2f(key)
	
	@JvmStatic
	fun putVec3d(nbt: NbtCompound, key: String, value: Vec3d) = nbt.putVec3d(key, value)
	
	@JvmStatic
	fun getVec3d(nbt: NbtCompound, key: String) = nbt.getVec3d(key)
	
	@JvmStatic
	fun putVec3f(nbt: NbtCompound, key: String, value: Vec3f) = nbt.putVec3f(key, value)
	
	@JvmStatic
	fun getVec3f(nbt: NbtCompound, key: String) = nbt.getVec3f(key)
	
	@JvmStatic
	fun putVec3i(nbt: NbtCompound, key: String, value: Vec3i) = nbt.putVec3i(key, value)
	
	@JvmStatic
	fun getVec3i(nbt: NbtCompound, key: String) = nbt.getVec3i(key)
	
	@JvmStatic
	fun putVector2f(nbt: NbtCompound, key: String, value: Vector2f) = nbt.putVector2f(key, value)
	
	@JvmStatic
	fun getVector2f(nbt: NbtCompound, key: String) = nbt.getVector2f(key)
	
	@JvmStatic
	fun putVector3d(nbt: NbtCompound, key: String, value: Vector3d) = nbt.putVector3d(key, value)
	
	@JvmStatic
	fun getVector3d(nbt: NbtCompound, key: String) = nbt.getVector3d(key)
	
	@JvmStatic
	fun putVector4f(nbt: NbtCompound, key: String, value: Vector4f) = nbt.putVector4f(key, value)
	
	@JvmStatic
	fun getVector4f(nbt: NbtCompound, key: String) = nbt.getVector4f(key)
	
	@JvmStatic
	fun putQuaternion(nbt: NbtCompound, key: String, value: Quaternion) = nbt.putQuaternion(key, value)
	
	@JvmStatic
	fun getQuaternion(nbt: NbtCompound, key: String) = nbt.getQuaternion(key)
}