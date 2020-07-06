package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.H
import dev.vini2003.hammer.common.util.extension.*
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.util.math.Vector2f
import net.minecraft.client.util.math.Vector3d
import net.minecraft.network.PacketByteBuf
import net.minecraft.tag.TagKey
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryKey

object BufUtils {
	inline fun <reified T> toPacketByteBuf(packet: T): PacketByteBuf {
		val buf = PacketByteBufs.create()
		buf.writeByteArray(H.PROTOBUF.encodeToByteArray(packet))
		
		return buf
	}
	
	inline fun <reified T> fromPacketByteBuf(buf: PacketByteBuf): T {
		return H.PROTOBUF.decodeFromByteArray(buf.readByteArray())
	}
	
	@JvmStatic
	fun writeBlockPos(buf: PacketByteBuf, value: BlockPos) = buf.writeBlockPos(value)
	
	@JvmStatic
	fun readBlockPos(buf: PacketByteBuf) = buf.readBlockPos()
	
	@JvmStatic
	fun writeChunkPos(buf: PacketByteBuf, value: ChunkPos) = buf.writeChunkPos(value)
	
	@JvmStatic
	fun readChunkPos(buf: PacketByteBuf) = buf.readChunkPos()
	
	@JvmStatic
	fun writeIdentifier(buf: PacketByteBuf, value: Identifier) = buf.writeIdentifier(value)
	
	@JvmStatic
	fun readIdentifier(buf: PacketByteBuf) = buf.readIdentifier()
	
	@JvmStatic
	fun <T> writeRegistryKey(buf: PacketByteBuf, value: RegistryKey<T>) = buf.writeRegistryKey(value)
	
	@JvmStatic
	fun <T> readRegistryKey(buf: PacketByteBuf) = buf.readRegistryKey<T>()
	
	@JvmStatic
	fun <T> writeTagKey(buf: PacketByteBuf, value: TagKey<T>) = buf.writeTagKey(value)
	
	@JvmStatic
	fun <T> readTagKey(buf: PacketByteBuf) = buf.readTagKey<T>()
	
	@JvmStatic
	fun writeVec2f(buf: PacketByteBuf, value: Vec2f) = buf.writeVec2f(value)
	
	@JvmStatic
	fun readVec2f(buf: PacketByteBuf) = buf.readVec2f()
	
	@JvmStatic
	fun writeVec3d(buf: PacketByteBuf, value: Vec3d) = buf.writeVec3d(value)
	
	@JvmStatic
	fun readVec3d(buf: PacketByteBuf) = buf.readVec3d()
	
	@JvmStatic
	fun writeVec3f(buf: PacketByteBuf, value: Vec3f) = buf.writeVec3f(value)
	
	@JvmStatic
	fun readVec3f(buf: PacketByteBuf) = buf.readVec3f()
	
	@JvmStatic
	fun writeVec3i(buf: PacketByteBuf, value: Vec3i) = buf.writeVec3i(value)
	
	@JvmStatic
	fun readVec3i(buf: PacketByteBuf) = buf.readVec3i()
	
	@JvmStatic
	fun writeVector2f(buf: PacketByteBuf, value: Vector2f) = buf.writeVector2f(value)
	
	@JvmStatic
	fun readVector2f(buf: PacketByteBuf) = buf.readVector2f()
	
	@JvmStatic
	fun writeVector3d(buf: PacketByteBuf, value: Vector3d) = buf.writeVector3d(value)
	
	@JvmStatic
	fun readVector3d(buf: PacketByteBuf) = buf.readVector3d()
	
	@JvmStatic
	fun writeVector4f(buf: PacketByteBuf, value: Vector4f) = buf.writeVector4f(value)
	
	@JvmStatic
	fun readVector4f(buf: PacketByteBuf) = buf.readVector4f()
	
	@JvmStatic
	fun writeQuaternion(buf: PacketByteBuf, value: Quaternion) = buf.writeQuaternion(value)
	
	@JvmStatic
	fun readQuaternion(buf: PacketByteBuf) = buf.readQuaternion()
}