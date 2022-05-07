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

package dev.vini2003.hammer.core.api.common.util

import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.size.Size
import dev.vini2003.hammer.core.api.common.util.extension.*
import net.minecraft.client.util.math.Vector2f
import net.minecraft.client.util.math.Vector3d
import net.minecraft.nbt.NbtCompound
import net.minecraft.tag.TagKey
import net.minecraft.util.math.*
import net.minecraft.util.registry.RegistryKey

object NbtUtils {
	@JvmStatic
	fun putPosition(nbt: NbtCompound, key: String, value: Position) = nbt.putPosition(key, value)
	
	@JvmStatic
	fun getPosition(nbt: NbtCompound, key: String) = nbt.getPosition(key)
	
	@JvmStatic
	fun putSize(nbt: NbtCompound, key: String, value: Size) = nbt.putSize(key, value)
	
	@JvmStatic
	fun getSize(nbt: NbtCompound, key: String) = nbt.getSize(key)
	
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