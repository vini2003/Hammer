/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.core.api.common.util.extension

import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.size.Size
import net.minecraft.client.util.math.Vector2f
import net.minecraft.client.util.math.Vector3d
import net.minecraft.network.PacketByteBuf
import net.minecraft.tag.TagKey
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryKey

fun PacketByteBuf.writePosition(value: Position) {
	writeFloat(value.x)
	writeFloat(value.y)
	writeFloat(value.z)
}

fun PacketByteBuf.readPosition(): Position {
	return Position(readFloat(), readFloat(), readFloat())
}

fun PacketByteBuf.writeSize(value: Size) {
	writeFloat(value.width)
	writeFloat(value.height)
	writeFloat(value.length)
}

fun PacketByteBuf.readSize(): Size {
	return Size(readFloat(), readFloat(), readFloat())
}

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