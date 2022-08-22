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

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.RegistryKey;

public class BufUtil {
	public static <T> void writeRegistryKey(PacketByteBuf buf, RegistryKey<T> value) {
		buf.writeIdentifier(value.getRegistry());
		buf.writeIdentifier(value.getValue());
	}
	
	public static <T> RegistryKey<T> readRegistryKey(PacketByteBuf buf) {
		var registryId = buf.readIdentifier();
		var id = buf.readIdentifier();
		
		return RegistryKey.of(RegistryKey.ofRegistry(registryId), id);
	}
	
	public static <T> void writeTagKey(PacketByteBuf buf, TagKey<T> value) {
		buf.writeIdentifier(value.registry().getValue());
		buf.writeIdentifier(value.id());
	}
	
	public static <T> TagKey<T> readTagKey(PacketByteBuf buf) {
		var registryId = buf.readIdentifier();
		var tagId = buf.readIdentifier();
		
		return TagKey.of(RegistryKey.ofRegistry(registryId), tagId);
	}
	
	public static void writeVec2f(PacketByteBuf buf, Vec2f value) {
		buf.writeFloat(value.x);
		buf.writeFloat(value.y);
	}
	
	public static Vec2f readVec2f(PacketByteBuf buf) {
		return new Vec2f(buf.readFloat(), buf.readFloat());
	}
	
	public static void writeVec3d(PacketByteBuf buf, Vec3d value) {
		buf.writeDouble(value.x);
		buf.writeDouble(value.y);
		buf.writeDouble(value.z);
	}
	
	public static Vec3d readVec3d(PacketByteBuf buf) {
		return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
	
	public static void writeVec3f(PacketByteBuf buf, Vec3f value) {
		buf.writeFloat(value.getX());
		buf.writeFloat(value.getY());
		buf.writeFloat(value.getZ());
	}
	
	public static Vec3f readVec3f(PacketByteBuf buf) {
		return new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	public static void writeVec3i(PacketByteBuf buf, Vec3i value) {
		buf.writeInt(value.getX());
		buf.writeInt(value.getY());
		buf.writeInt(value.getZ());
	}
	
	public static Vec3i readVec3i(PacketByteBuf buf) {
		return new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	public static void writeVector2f(PacketByteBuf buf, Vector2f value) {
		buf.writeFloat(value.getX());
		buf.writeFloat(value.getY());
	}
	
	public static Vector2f readVector2f(PacketByteBuf buf) {
		return new Vector2f(buf.readFloat(), buf.readFloat());
	}
	
	public static void writeVector3d(PacketByteBuf buf, Vector3d value) {
		buf.writeDouble(value.x);
		buf.writeDouble(value.y);
		buf.writeDouble(value.z);
	}
	
	public static Vector3d readVector3d(PacketByteBuf buf) {
		return new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
	
	public static void writeVector4f(PacketByteBuf buf, Vector4f value) {
		buf.writeFloat(value.getX());
		buf.writeFloat(value.getY());
		buf.writeFloat(value.getZ());
		buf.writeFloat(value.getW());
	}
	
	public static Vector4f readVector4f(PacketByteBuf buf) {
		return new Vector4f(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	public static void writeQuaternion(PacketByteBuf buf, Quaternion value) {
		buf.writeFloat(value.getX());
		buf.writeFloat(value.getY());
		buf.writeFloat(value.getZ());
		buf.writeFloat(value.getW());
	}
	
	public static Quaternion readQuaternion(PacketByteBuf buf) {
		return new Quaternion(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
}
