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

package dev.vini2003.hammer.core.api.common.util;

import com.mojang.brigadier.StringReader;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryKey;

public class NbtUtil {
	public static NbtCompound parseNbt(String nbtString) {
		try {
			return new StringNbtReader(new StringReader(nbtString)).parseCompound();
		} catch (Exception ignore) {
			return new NbtCompound();
		}
	}
	
	public static void putColor(NbtCompound nbt, String key, Color value) {
		nbt.putLong(key + "Rgba", value.toRgba());
	}
	
	public static Color getColor(NbtCompound nbt, String key) {
		return new Color(nbt.getLong(key + "Rgba"));
	}
	
	public static void putPosition(NbtCompound nbt, String key, Position value) {
		nbt.putFloat(key + "X", value.getX());
		nbt.putFloat(key + "Y", value.getY());
		nbt.putFloat(key + "Z", value.getZ());
	}
	
	public static Position getPosition(NbtCompound nbt, String key) {
		return new Position(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"), nbt.getFloat(key + "Z"));
	}
	
	public static void putSize(NbtCompound nbt, String key, Size value) {
		nbt.putFloat(key + "Width", value.getWidth());
		nbt.putFloat(key + "Height", value.getHeight());
		nbt.putFloat(key + "Length", value.getHeight());
	}
	
	public static Size getSize(NbtCompound nbt, String key) {
		return new Size(nbt.getFloat(key + "Width"), nbt.getFloat(key + "Height"), nbt.getFloat(key + "Length"));
	}
	
	public static void putBlockPos(NbtCompound nbt, String key, BlockPos value) {
		nbt.putInt(key + "X", value.getX());
		nbt.putInt(key + "Y", value.getY());
		nbt.putInt(key + "Z", value.getZ());
	}
	
	public static BlockPos getBlockPos(NbtCompound nbt, String key) {
		return new BlockPos(nbt.getInt(key + "X"), nbt.getInt(key + "Y"), nbt.getInt(key + "Z"));
	}
	
	public static void putChunkPos(NbtCompound nbt, String key, ChunkPos value) {
		nbt.putInt(key + "X", value.x);
		nbt.putInt(key + "Z", value.z);
	}
	
	public static ChunkPos getChunkPos(NbtCompound nbt, String key) {
		return new ChunkPos(nbt.getInt(key + "X"), nbt.getInt(key + "Z"));
	}
	
	public static void putIdentifier(NbtCompound nbt, String key, Identifier value) {
		nbt.putString(key, value.toString());
	}
	
	public static Identifier getIdentifier(NbtCompound nbt, String key) {
		return new Identifier(nbt.getString(key));
	}
	
	public static <T> void putRegistryKey(NbtCompound nbt, String key, RegistryKey<T> value) {
		nbt.putString(key + "RegistryId", value.method_41185().toString());
		nbt.putString(key + "Id", value.getValue().toString());
	}
	
	public static <T> RegistryKey<T> getRegistryKey(NbtCompound nbt, String key) {
		var registryId = new Identifier(nbt.getString(key + "RegistryId"));
		var id = new Identifier(nbt.getString(key + "Id"));
		
		return RegistryKey.of(RegistryKey.ofRegistry(registryId), id);
	}
	
	public static <T> void putTagKey(NbtCompound nbt, String key, TagKey<T> value) {
		nbt.putString(key + "RegistryId", value.registry().getValue().toString());
		nbt.putString(key + "TagId", value.id().toString());
	}
	
	public static <T> TagKey<T> getTagKey(NbtCompound nbt, String key) {
		var registryId = new Identifier(nbt.getString(key + "RegistryId"));
		var tagId = new Identifier(nbt.getString(key + "TagKey"));
		
		return TagKey.of(RegistryKey.ofRegistry(registryId), tagId);
	}
	
	public static void putVec2f(NbtCompound nbt, String key, Vec2f value) {
		nbt.putFloat(key + "X", value.x);
		nbt.putFloat(key + "Y", value.y);
	}
	
	public static Vec2f getVec2f(NbtCompound nbt, String key) {
		return new Vec2f(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"));
	}
	
	public static void putVec3d(NbtCompound nbt, String key, Vec3d value) {
		nbt.putDouble(key + "X", value.x);
		nbt.putDouble(key + "Y", value.y);
		nbt.putDouble(key + "Z", value.z);
	}
	
	public static Vec3d getVec3d(NbtCompound nbt, String key) {
		return new Vec3d(nbt.getDouble(key + "X"), nbt.getDouble(key + "Y"), nbt.getDouble(key + "Z"));
	}
	
	public static void putVec3f(NbtCompound nbt, String key, Vec3f value) {
		nbt.putFloat(key + "X", value.getX());
		nbt.putFloat(key + "Y", value.getX());
		nbt.putFloat(key + "Z", value.getZ());
	}
	
	public static Vec3f getVec3f(NbtCompound nbt, String key) {
		return new Vec3f(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"), nbt.getFloat(key + "Z"));
	}
	
	public static void putVec3i(NbtCompound nbt, String key, Vec3i value) {
		nbt.putInt(key + "X", value.getX());
		nbt.putInt(key + "Y", value.getY());
		nbt.putInt(key + "Z", value.getZ());
	}
	
	public static Vec3i getVec3i(NbtCompound nbt, String key) {
		return new Vec3i(nbt.getInt(key + "X"), nbt.getInt(key + "Y"), nbt.getInt(key + "Z"));
	}
	
	public static void putVector2f(NbtCompound nbt, String key, Vector2f value) {
		nbt.putFloat(key + "X", value.getX());
		nbt.putFloat(key + "Y", value.getY());
	}
	
	public static Vector2f getVector2f(NbtCompound nbt, String key) {
		return new Vector2f(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"));
	}
	
	public static void putVector3d(NbtCompound nbt, String key, Vector3d value) {
		nbt.putDouble(key + "X", value.x);
		nbt.putDouble(key + "Y", value.y);
		nbt.putDouble(key + "Z", value.z);
	}
	
	public static Vector3d getVector3d(NbtCompound nbt, String key) {
		return new Vector3d(nbt.getDouble(key + "X"), nbt.getDouble(key + "Y"), nbt.getDouble(key + "Z"));
	}
	
	public static void putVector4f(NbtCompound nbt, String key, Vector4f value) {
		nbt.putFloat(key + "X", value.getX());
		nbt.putFloat(key + "Y", value.getY());
		nbt.putFloat(key + "Z", value.getZ());
		nbt.putFloat(key + "W", value.getW());
	}
	
	public static Vector4f getVector4f(NbtCompound nbt, String key) {
		return new Vector4f(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"), nbt.getFloat(key + "Z"), nbt.getFloat(key + "W"));
	}
	
	public static void putQuaternion(NbtCompound nbt, String key, Quaternion value) {
		nbt.putFloat(key + "X", value.getX());
		nbt.putFloat(key + "Y", value.getY());
		nbt.putFloat(key + "Z", value.getZ());
		nbt.putFloat(key + "W", value.getW());
	}
	
	public static Quaternion getQuaternion(NbtCompound nbt, String key) {
		return new Quaternion(nbt.getFloat(key + "X"), nbt.getFloat(key + "Y"), nbt.getFloat(key + "Z"), nbt.getFloat(key + "W"));
	}
}
