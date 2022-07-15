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

package dev.vini2003.hammer.core.api.common.math.position;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * <p>A {@link Position} represents a three-dimensional position.
 *
 * <p>The following serialization methods are provided:</p>
 * <ul>
 *     <li>{@link #toJson(Position)} - from {@link Position} to {@link JsonElement}.</li>
 *     <li>{@link #toNbt(Position)} - from {@link Position} to {@link NbtCompound}.</li>
 *     <li>{@link #toBuf(Position, PacketByteBuf)} - from {@link Position} to {@link PacketByteBuf}.</li>
 * </ul>
 
 * <ul>
 *     <li>{@link #fromJson(JsonElement)} - from {@link JsonElement} to {@link Position}.</li>
 *     <li>{@link #fromNbt(NbtCompound)} - from {@link NbtCompound} to {@link Position}.</li>
 *     <li>{@link #fromBuf(PacketByteBuf)} - from {@link PacketByteBuf} to {@link Position}.</li>
 * </ul>
 */
public class Position implements PositionHolder {
	private final float x;
	private final float y;
	private final float z;
	
	/**
	 * Serializes a position to a {@link PacketByteBuf}.
	 * @param position The position to serialize.
	 * @param buf The buffer to serialize to.
	 * @return The buffer.
	 */
	public static PacketByteBuf toBuf(Position position, PacketByteBuf buf) {
		buf.writeFloat(position.getX());
		buf.writeFloat(position.getY());
		buf.writeFloat(position.getZ());
		
		return buf;
	}
	
	/**
	 * Deserializes a position from a {@link PacketByteBuf}.
	 * @param buf The buffer to deserialize from.
	 * @return The position.
	 */
	public static Position fromBuf(PacketByteBuf buf) {
		return new Position(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}
	
	/**
	 * Serializes a position to an {@link NbtCompound}.
	 * @param position The position.
	 * @return The serialized position.
	 */
	public static NbtCompound toNbt(Position position) {
		var nbt = new NbtCompound();
		
		nbt.putFloat("x", position.getX());
		nbt.putFloat("y", position.getY());
		nbt.putFloat("z", position.getZ());
		
		return nbt;
	}
	
	/**
	 * Deserializes a position from an {@link NbtCompound}.
	 * @param nbt The serialized position.
	 * @return The position.
	 */
	public static Position fromNbt(NbtCompound nbt) {
		return new Position(nbt.getFloat("x"), nbt.getFloat("y"), nbt.getFloat("z"));
	}
	
	/**
	 * Serializes a position to a {@link JsonElement}.
	 * @param position The position.
	 * @return The serialized position.
	 */
	public static JsonElement toJson(Position position) {
		var json = new JsonObject();
		
		json.addProperty("x", position.getX());
		json.addProperty("y", position.getY());
		json.addProperty("z", position.getZ());
		
		return json;
	}
	
	/**
	 * Deserializes a position from a {@link JsonElement}.
	 * @param json The serialized position.
	 * @return The position.
	 */
	public static Position fromJson(JsonElement json) {
		var object = json.getAsJsonObject();
		
		return new Position(object.get("x").getAsFloat(), object.get("y").getAsFloat(), object.get("z").getAsFloat());
	}
	
	/**
	 * Returns a collection of all positions within the two given positions.
	 *
	 * @param startPosition the start position.
	 * @param endPosition   the end position.
	 *
	 * @return the collection.
	 */
	public static Collection<Position> collect(Position startPosition, Position endPosition) {
		var minPosition = new Position(Math.min(startPosition.x, endPosition.x), Math.min(startPosition.y, endPosition.y), Math.min(startPosition.z, endPosition.z));
		var maxPosition = new Position(Math.max(startPosition.x, endPosition.y), Math.max(startPosition.y, endPosition.y), Math.max(startPosition.z, endPosition.z));
		
		var x = (int) minPosition.x;
		var y = (int) minPosition.y;
		var z = (int) minPosition.z;
		
		var positions = new ArrayList<Position>();
		
		while (x < maxPosition.x) {
			while (y < maxPosition.y) {
				while (z < maxPosition.z) {
					positions.add(new Position(x, y, z));
					
					++z;
				}
				
				++y;
			}
			
			++x;
		}
		
		return positions;
	}
	
	/**
	 * Constructs a position.
	 *
	 * @param x the X position component.
	 * @param y the Y position component.
	 * @param z the Z position component.
	 *
	 * @return the position.
	 */
	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructs a position.
	 *
	 * @param x the X position component.
	 * @param y the Y position component.
	 *
	 * @return the position.
	 */
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0.0F;
	}
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor           the anchor.
	 * @param relativePosition the relative position.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor, Position relativePosition) {
		this(anchor.getX() + relativePosition.getX(), anchor.getY() + relativePosition.getY(), anchor.getZ() + relativePosition.getZ());
	}
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 * @param relativeZ the relative Z component.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor, float relativeX, float relativeY, float relativeZ) {
		this(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ() + relativeZ);
	}
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor, float relativeX, float relativeY) {
		this(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ());
	}
	
	/**
	 * Constructs an anchor's position.
	 *
	 * @param anchor the anchor.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor) {
		this(anchor.getX(), anchor.getY(), anchor.getZ());
	}
	
	/**
	 * Converts a {@link BlockPos} to a position.
	 *
	 * @param blockPos the block pos.
	 *
	 * @return the position.
	 */
	public Position(BlockPos blockPos) {
		this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	/**
	 * Converts a {@link ChunkPos} to a position.
	 *
	 * @param chunkPos the chunk pos.
	 *
	 * @return the position.
	 */
	public Position(ChunkPos chunkPos) {
		this(chunkPos.x, 0.0F, chunkPos.z);
	}
	
	/**
	 * Converts a {@link Vec2f} to a position.
	 * @param vec2f The vector.
	 * @return The position.
	 */
	public Position(Vec2f vec2f) {
		this(vec2f.x, vec2f.y);
	}
	
	/**
	 * Converts a {@link Vec3d} to a position.
	 * @param vec3d The vector.
	 * @return The position.
	 */
	public Position(Vec3d vec3d) {
		this((float) vec3d.x, (float) vec3d.y, (float) vec3d.z);
	}
	
	/**
	 * Converts a {@link Vec3f} to a position.
	 * @param vec3f The vector.
	 * @return The position.
	 */
	public Position(Vec3f vec3f) {
		this(vec3f.getX(), vec3f.getY(), vec3f.getZ());
	}
	
	/**
	 * Converts a {@link Vec3i} to a position.
	 * @param vec3i The vector.
	 * @return The position.
	 */
	public Position(Vec3i vec3i) {
		this(vec3i.getX(), vec3i.getY(), vec3i.getZ());
	}
	
	/**
	 * Converts a {@link Vector2f} to a position.
	 * @param vector2f The vector.
	 * @return The position.
	 */
	public Position(Vector2f vector2f) {
		this(vector2f.getX(), vector2f.getY());
	}
	
	/**
	 * Converts a {@link Vector3d} to a position.
	 * @param vector3d The vector.
	 * @return The position.
	 */
	 public Position(Vector3d vector3d) {
		this((float) vector3d.x, (float) vector3d.y, (float) vector3d.z);
	 }
	
	/**
	 * Converts this position to a {@link BlockPos}.
	 *
	 * @return the block pos.
	 */
	public BlockPos toBlockPos() {
		return new BlockPos(x, y, z);
	}
	
	/**
	 * Converts this position to a {@link ChunkPos}
	 *
	 * @return the chunk pos.
	 */
	public ChunkPos toChunkPos() {
		return new ChunkPos((int) x, (int) z);
	}
	
	/**
	 * Converts this position to a {@link Vec2f}.
	 * @return The vector.
	 */
	public Vec2f toVec2f() {
		return new Vec2f(x, y);
	}
	
	/**
	 * Converts this position to a {@link Vec3d}.
	 * @return The vector.
	 */
	public Vec3d toVec3d() {
		return new Vec3d(x, y, z);
	}
	
	/**
	 * Converts this position to a {@link Vec3f}.
	 * @return The vector.
	 */
	public Vec3f toVec3f() {
		return new Vec3f(x, y, z);
	}
	
	/**
	 * Converts this position to a {@link Vec3i}.
	 * @return The vector.
	 */
	public Vec3i toVec3i() {
		return new Vec3i((int) x, (int) y, (int) z);
	}
	
	/**
	 * Converts this position to a {@link Vector2f}.
	 * @return The vector.
	 */
	public Vector2f toVector2f() {
		return new Vector2f(x, y);
	}
	
	/**
	 * Converts this position to a {@link Vector3d}.
	 * @return The vector.
	 */
	public Vector3d toVector3d() {
		return new Vector3d(x, y, z);
	}
	
	/**
	 * Returns the distance between this and the distant position.
	 *
	 * @param position the distant position.
	 *
	 * @return the distance.
	 */
	public float distanceTo(Position position) {
		return (float) Math.sqrt(Math.pow((x - position.x), 2.0D) + Math.pow((y - position.y), 2.0D) + Math.pow((z - position.z), 2.0D));
	}
	
	/**
	 * Returns the squared distance between this and the distant position.
	 *
	 * @param position the distant position.
	 *
	 * @return the distance.
	 */
	public float squaredDistanceTo(Position position) {
		return (float) (Math.pow((x - position.x), 2.0D) + Math.pow((y - position.y), 2.0D) + Math.pow((z - position.z), 2.0D));
	}
	
	/**
	 * Returns this position offset by X, Y and Z components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 * @param z the Z component to offset this position by.
	 *
	 * @return the resulting position.
	 */
	public Position offset(float x, float y, float z) {
		return new Position(this.x + x, this.y + y, this.z + z);
	}
	
	/**
	 * Returns this position offset by X and Y components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 *
	 * @return the resulting position.
	 */
	public Position offset(float x, float y) {
		return new Position(this.x + x, this.y + y, this.z);
	}
	
	/**
	 * Returns this position, adding another position to it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public Position plus(Position position) {
		return new Position(x + position.x, y + position.y, z + position.z);
	}
	
	/**
	 * Returns this position, subtracting another position from it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public Position minus(Position position) {
		return new Position(x + position.x, y + position.y, z + position.z);
	}
	
	/**
	 * Returns this position, multiplying its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public Position times(float number) {
		return new Position(x * number, y * number, z * number);
	}
	
	/**
	 * Returns this position, dividing its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public Position div(float number) {
		return new Position(x / number, y / number, z / number);
	}
	
	/**
	 * Returns this position, rotated by the given quaternion.
	 *
	 * @return the resulting position.
	 */
	public Position rotate(Quaternion quaternion) {
		var q1 = new Quaternion(quaternion);
		q1.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0F));
		var q2 = new Quaternion(quaternion);
		q2.conjugate();
		q1.hamiltonProduct(q2);
		
		return new Position(q1.getX(), q1.getY(), q1.getZ());
	}
	
	/**
	 * Returns the maximum position by the given function.
	 *
	 * @return the maximum position.
	 */
	public static Position maxBy(Position a, Position b, Function<Position, Float> function) {
		var resA = function.apply(a);
		var resB = function.apply(b);
		
		if (resA >= resB) {
			return a;
		} else {
			return b;
		}
	}
	
	/**
	 * Returns the minimum position by the given function.
	 *
	 * @return the minimum position.
	 */
	public static Position minBy(Position a, Position b, Function<Position, Float> function) {
		var resA = function.apply(a);
		var resB = function.apply(b);
		
		if (resA <= resB) {
			return a;
		} else {
			return b;
		}
	}
	
	/**
	 * Returns the maximum position.
	 *
	 * @return the maximum position.
	 */
	public static Position max(Position a, Position b) {
		return new Position(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
	}
	
	/**
	 * Returns the minimum position.
	 *
	 * @return the minimum position.
	 */
	public static Position min(Position a, Position b) {
		return new Position(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
	}
	
	@Override
	public float getX() {
		return x;
	}
	
	@Override
	public float getY() {
		return y;
	}
	
	@Override
	public float getZ() {
		return z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Position)) {
			return false;
		}
		
		var position = (Position) o;
		
		return Float.compare(position.x, x) == 0 && Float.compare(position.y, y) == 0 && Float.compare(position.z, z) == 0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
