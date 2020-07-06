package dev.vini2003.hammer.common.util.extension

import net.minecraft.client.util.math.Vector2f
import net.minecraft.client.util.math.Vector3d
import net.minecraft.network.PacketByteBuf
import net.minecraft.tag.TagKey
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryKey

fun <T> PacketByteBuf.writeRegistryKey(value: RegistryKey<T>) {
	writeIdentifier(value.method_41185())
	writeIdentifier(value.value)
}

fun <T> PacketByteBuf.readRegistryKey(): RegistryKey<T> {
	val registryId = readIdentifier()
	val id = readIdentifier()
	
	return RegistryKey.of(RegistryKey.ofRegistry(registryId), id)
}

fun <T> PacketByteBuf.writeTagKey(value: TagKey<T>) {
	writeIdentifier(value.registry.value)
	writeIdentifier(value.id)
}

fun <T> PacketByteBuf.readTagKey(): TagKey<T> {
	val registryId = readIdentifier()
	val tagId = readIdentifier()
	
	return TagKey.of(RegistryKey.ofRegistry(registryId), tagId)
}

fun PacketByteBuf.writeVec2f(value: Vec2f) {
	writeFloat(value.x)
	writeFloat(value.y)
}

fun PacketByteBuf.readVec2f(): Vec2f {
	return Vec2f(readFloat(), readFloat())
}

fun PacketByteBuf.writeVec3d(value: Vec3d) {
	writeDouble(value.x)
	writeDouble(value.y)
	writeDouble(value.z)
}

fun PacketByteBuf.readVec3d(): Vec3d {
	return Vec3d(readDouble(), readDouble(), readDouble())
}

fun PacketByteBuf.writeVec3f(value: Vec3f) {
	writeFloat(value.x)
	writeFloat(value.y)
	writeFloat(value.z)
}

fun PacketByteBuf.readVec3f(): Vec3f {
	return Vec3f(readFloat(), readFloat(), readFloat())
}

fun PacketByteBuf.writeVec3i(value: Vec3i) {
	writeInt(value.x)
	writeInt(value.y)
	writeInt(value.z)
}

fun PacketByteBuf.readVec3i(): Vec3i {
	return Vec3i(readInt(), readInt(), readInt())
}

fun PacketByteBuf.writeVector2f(value: Vector2f) {
	writeFloat(value.x)
	writeFloat(value.y)
}

fun PacketByteBuf.readVector2f(): Vector2f {
	return Vector2f(readFloat(), readFloat())
}

fun PacketByteBuf.writeVector3d(value: Vector3d) {
	writeDouble(value.x)
	writeDouble(value.y)
	writeDouble(value.z)
}

fun PacketByteBuf.readVector3d(): Vector3d {
	return Vector3d(readDouble(), readDouble(), readDouble())
}

fun PacketByteBuf.writeVector4f(value: Vector4f) {
	writeFloat(value.x)
	writeFloat(value.y)
	writeFloat(value.z)
	writeFloat(value.w)
}

fun PacketByteBuf.readVector4f(): Vector4f {
	return Vector4f(readFloat(), readFloat(), readFloat(), readFloat())
}

fun PacketByteBuf.writeQuaternion(value: Quaternion) {
	writeFloat(value.x)
	writeFloat(value.y)
	writeFloat(value.z)
	writeFloat(value.w)
}

fun PacketByteBuf.readQuaternion(): Quaternion {
	return Quaternion(readFloat(), readFloat(), readFloat(), readFloat())
}